package leoszr.finance_app.budget.service;

import leoszr.finance_app.budget.dto.*;
import leoszr.finance_app.budget.entity.*;
import leoszr.finance_app.budget.repository.BudgetRepository;
import leoszr.finance_app.category.entity.Category;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.*;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class BudgetService {
	private static final BigDecimal WARNING_RATIO = new BigDecimal("0.80");
	private final BudgetRepository budgets; private final CategoryRepository categories; private final UserRepository users; private final TransactionRepository transactions; private final BudgetCycleService cycles;
	public BudgetService(BudgetRepository budgets, CategoryRepository categories, UserRepository users, TransactionRepository transactions, BudgetCycleService cycles){this.budgets=budgets;this.categories=categories;this.users=users;this.transactions=transactions;this.cycles=cycles;}
	@Transactional public BudgetResponse create(UUID userId, BudgetRequest req){ if(budgets.existsByUserIdAndActiveTrue(userId)) throw new BusinessRuleException("Budget ativo já existe."); User user=users.findById(userId).orElseThrow(() -> new NotFoundException("Usuário não encontrado.")); Budget b=new Budget(); b.setUser(user); apply(b,userId,req); return toResponse(budgets.save(b)); }
	@Transactional(readOnly=true) public BudgetResponse getActive(UUID userId){ return toResponse(active(userId)); }
	@Transactional public BudgetResponse update(UUID userId, BudgetRequest req){ Budget b=active(userId); apply(b,userId,req); return toResponse(b); }
	@Transactional(readOnly=true) public BudgetUsageResponse currentCycle(UUID userId, LocalDate reference){ Budget b=active(userId); BudgetCycleRange range=cycles.currentCycle(b.getCycleType(), b.getCycleStartDate(), reference==null?LocalDate.now():reference); BigDecimal used=transactions.sumExpensesInPeriod(userId, range.startDate(), range.endDate()); BudgetStatus status=status(used,b.getTotalAmount()); return new BudgetUsageResponse(b.getId(),range.startDate(),range.endDate(),b.getTotalAmount(),used,status); }
	private Budget active(UUID userId){ return budgets.findActiveByUserId(userId).orElseThrow(() -> new NotFoundException("Budget não encontrado.")); }
	private void apply(Budget b, UUID userId, BudgetRequest req){ BigDecimal total=MoneyUtils.requirePositive(req.totalAmount()); List<BudgetCategoryRequest> reqCats=req.categories()==null?List.of():req.categories(); BigDecimal sum=BigDecimal.ZERO; Set<UUID> seen=new HashSet<>(); List<BudgetCategory> newCats=new ArrayList<>(); for(BudgetCategoryRequest cr:reqCats){ if(!seen.add(cr.categoryId())) throw new BusinessRuleException("Categoria duplicada no budget."); BigDecimal amount=MoneyUtils.requirePositive(cr.amount()); sum=sum.add(amount); Category c=categories.findByIdAndUserId(cr.categoryId(), userId).orElseThrow(() -> new NotFoundException("Categoria não encontrada.")); BudgetCategory bc=new BudgetCategory(); bc.setBudget(b); bc.setCategory(c); bc.setAmount(amount); newCats.add(bc); } if(sum.compareTo(total)>0) throw new BusinessRuleException("Soma das categorias excede o total."); b.setTotalAmount(total); b.setCycleType(req.cycleType()); b.setCycleStartDate(req.cycleStartDate()); b.getCategories().clear(); b.getCategories().addAll(newCats); }
	private BudgetStatus status(BigDecimal used, BigDecimal total){ if(used.compareTo(total)>0) return BudgetStatus.EXCEEDED; if(used.compareTo(total.multiply(WARNING_RATIO))>=0) return BudgetStatus.WARNING; return BudgetStatus.NORMAL; }
	private BudgetResponse toResponse(Budget b){ return new BudgetResponse(b.getId(), b.getTotalAmount(), b.getCycleType(), b.getCycleStartDate(), b.isActive(), b.getCategories().stream().map(c -> new BudgetCategoryResponse(c.getCategory().getId(), c.getCategory().getName(), c.getAmount())).toList()); }
}
