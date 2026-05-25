package leoszr.finance_app.savingbox.service;

import leoszr.finance_app.common.*;
import leoszr.finance_app.savingbox.dto.*;
import leoszr.finance_app.savingbox.entity.SavingBox;
import leoszr.finance_app.savingbox.repository.SavingBoxRepository;
import leoszr.finance_app.transaction.dto.TransactionResponse;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class SavingBoxService {
	private final SavingBoxRepository boxes; private final UserRepository users; private final TransactionRepository transactions;
	public SavingBoxService(SavingBoxRepository boxes, UserRepository users, TransactionRepository transactions){this.boxes=boxes;this.users=users;this.transactions=transactions;}
	@Transactional public SavingBoxResponse create(UUID userId, SavingBoxRequest req){ User user=users.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); String norm=TextNormalizer.normalize(req.name()); if(boxes.existsByUserIdAndNormalizedNameAndDeletedAtIsNull(userId,norm)) throw new BusinessRuleException("Caixa já existe."); SavingBox b=new SavingBox(); b.setUser(user); b.setName(req.name().trim()); b.setNormalizedName(norm); b.setCurrency("BRL"); b.setBalance(BigDecimal.ZERO); b.setTargetAmount(validateTarget(req.targetAmount())); return toResponse(boxes.save(b)); }
	@Transactional public SavingBox ensureDefault(UUID userId){ return boxes.findByUserIdAndDefaultBoxTrueAndDeletedAtIsNull(userId).orElseGet(() -> { User user=users.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); SavingBox b=new SavingBox(); b.setUser(user); b.setName("Economias"); b.setNormalizedName(TextNormalizer.normalize("Economias")); b.setCurrency("BRL"); b.setBalance(BigDecimal.ZERO); b.setDefaultBox(true); return boxes.save(b); }); }
	@Transactional(readOnly=true) public List<SavingBoxResponse> list(UUID userId){ return boxes.findByUserIdAndDeletedAtIsNullOrderByDefaultBoxDescNameAsc(userId).stream().map(this::toResponse).toList(); }
	@Transactional(readOnly=true) public SavingBoxResponse get(UUID userId, UUID id){ return toResponse(active(userId,id)); }
	@Transactional public SavingBoxResponse update(UUID userId, UUID id, SavingBoxRequest req){ SavingBox b=active(userId,id); if(b.isDefaultBox()){ if(!"Economias".equals(req.name())) throw new BusinessRuleException("Caixa padrão não pode ser renomeada."); if(req.targetAmount()!=null) throw new BusinessRuleException("Caixa padrão não aceita meta."); } else { String norm=TextNormalizer.normalize(req.name()); if(boxes.existsByUserIdAndNormalizedNameAndDeletedAtIsNullAndIdNot(userId,norm,id)) throw new BusinessRuleException("Caixa já existe."); b.setName(req.name().trim()); b.setNormalizedName(norm); b.setTargetAmount(validateTarget(req.targetAmount())); } return toResponse(b); }
	@Transactional public void delete(UUID userId, UUID id){ SavingBox b=active(userId,id); if(b.isDefaultBox()) throw new BusinessRuleException("Caixa padrão não pode ser excluída."); b.setDeletedAt(Instant.now()); }
	@Transactional public SavingBox lock(UUID userId, UUID id){ return boxes.findActiveByIdAndUserIdForUpdate(id,userId).orElseThrow(() -> new NotFoundException("Caixa não encontrada.")); }
	@Transactional(readOnly=true) public Page<TransactionResponse> movements(UUID userId, UUID id, int page, int size){ active(userId,id); return transactions.findMovements(userId,id,PageRequest.of(page,size,Sort.by("occurredOn").descending())).map(t -> new TransactionResponse(t.getId(),t.getType(),t.getAmount(),t.getOccurredOn(),t.getDescription(),t.getCategory().getId(),t.getCategory().getName(),t.getCategory().getSpecialType(),t.getBoxId())); }
	private SavingBox active(UUID userId, UUID id){ return boxes.findByIdAndUserIdAndDeletedAtIsNull(id,userId).orElseThrow(() -> new NotFoundException("Caixa não encontrada.")); }
	private BigDecimal validateTarget(BigDecimal v){ return v==null?null:MoneyUtils.requirePositive(v); }
	private SavingBoxResponse toResponse(SavingBox b){ return new SavingBoxResponse(b.getId(),b.getName(),b.getCurrency(),b.getBalance(),b.getTargetAmount(),b.isDefaultBox()); }
}
