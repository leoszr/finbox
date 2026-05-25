package leoszr.finance_app.transaction.service;

import leoszr.finance_app.category.entity.*;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.*;
import leoszr.finance_app.savingbox.entity.SavingBox;
import leoszr.finance_app.savingbox.repository.SavingBoxRepository;
import leoszr.finance_app.transaction.dto.*;
import leoszr.finance_app.transaction.entity.*;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
	private final TransactionRepository transactions; private final CategoryRepository categories; private final UserRepository users; private final SavingBoxRepository boxes;
	public TransactionService(TransactionRepository transactions, CategoryRepository categories, UserRepository users, SavingBoxRepository boxes){this.transactions=transactions;this.categories=categories;this.users=users;this.boxes=boxes;}
	@Transactional public TransactionResponse create(UUID userId, TransactionRequest req){ User user=users.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); Transaction t=new Transaction(); t.setUser(user); apply(t,userId,req); applyBoxDelta(userId,t,1); return toResponse(transactions.save(t)); }
	@Transactional(readOnly=true) public TransactionResponse get(UUID userId, UUID id){ return toResponse(transactions.findByIdAndUserId(id,userId).orElseThrow(() -> new NotFoundException("Transação não encontrada."))); }
	@Transactional public TransactionResponse update(UUID userId, UUID id, TransactionRequest req){ Transaction t=transactions.findByIdAndUserId(id,userId).orElseThrow(() -> new NotFoundException("Transação não encontrada.")); applyBoxDelta(userId,t,-1); apply(t,userId,req); applyBoxDelta(userId,t,1); return toResponse(t); }
	@Transactional public void delete(UUID userId, UUID id){ Transaction t=transactions.findByIdAndUserId(id,userId).orElseThrow(() -> new NotFoundException("Transação não encontrada.")); applyBoxDelta(userId,t,-1); transactions.delete(t); }
	@Transactional(readOnly=true) public Page<TransactionResponse> list(UUID userId, LocalDate startDate, LocalDate endDate, UUID categoryId, TransactionType type, BigDecimal minAmount, BigDecimal maxAmount, String description, HistorySortType sortType, int page, int size){
		if (startDate!=null && endDate!=null && startDate.isAfter(endDate)) throw new BusinessRuleException("Período inválido.");
		Sort sort = switch (sortType == null ? HistorySortType.NEWEST_FIRST : sortType) { case OLDEST_FIRST -> Sort.by("occurredOn").ascending(); case HIGHEST_AMOUNT -> Sort.by("amount").descending(); case LOWEST_AMOUNT -> Sort.by("amount").ascending(); default -> Sort.by("occurredOn").descending(); };
		return transactions.search(userId,startDate,endDate,categoryId,type,minAmount,maxAmount,StringUtils.hasText(description)?description:null,PageRequest.of(page,size,sort)).map(this::toResponse);
	}
	private void apply(Transaction t, UUID userId, TransactionRequest req){
		t.setType(req.type()); t.setAmount(MoneyUtils.requirePositive(req.amount())); t.setOccurredOn(req.occurredOn()); t.setDescription(StringUtils.hasText(req.description()) ? req.description().trim() : null); t.setBoxId(null);
		Category c = req.categoryId()==null ? categories.findByUserIdAndSpecialType(userId, CategorySpecialType.UNCATEGORIZED).orElseThrow(() -> new NotFoundException("Categoria não encontrada.")) : categories.findByIdAndUserId(req.categoryId(), userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada."));
		if (c.getSpecialType()==CategorySpecialType.SAVED) { if(req.boxId()==null) throw new BusinessRuleException("Categoria Guardado exige caixa."); boxes.findActiveByIdAndUserIdForUpdate(req.boxId(),userId).orElseThrow(() -> new NotFoundException("Caixa não encontrada.")); t.setBoxId(req.boxId()); }
		else if(req.boxId()!=null) throw new BusinessRuleException("Categoria comum não aceita caixa.");
		t.setCategory(c);
	}
	private void applyBoxDelta(UUID userId, Transaction t, int direction){ if(t.getBoxId()==null) return; SavingBox b=boxes.findActiveByIdAndUserIdForUpdate(t.getBoxId(),userId).orElseThrow(() -> new NotFoundException("Caixa não encontrada.")); BigDecimal signed=t.getType()==TransactionType.INCOME?t.getAmount():t.getAmount().negate(); BigDecimal next=b.getBalance().add(signed.multiply(BigDecimal.valueOf(direction))); if(next.signum()<0) throw new BusinessRuleException("Saldo da caixa não pode ficar negativo."); b.setBalance(next); }
	private TransactionResponse toResponse(Transaction t){ Category c=t.getCategory(); return new TransactionResponse(t.getId(), t.getType(), t.getAmount(), t.getOccurredOn(), t.getDescription(), c.getId(), c.getName(), c.getSpecialType(), t.getBoxId()); }
}
