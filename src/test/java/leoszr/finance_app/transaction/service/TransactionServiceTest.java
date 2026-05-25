package leoszr.finance_app.transaction.service;

import leoszr.finance_app.category.entity.*;
import leoszr.finance_app.category.repository.CategoryRepository;
import leoszr.finance_app.common.*;
import leoszr.finance_app.transaction.dto.TransactionRequest;
import leoszr.finance_app.transaction.entity.*;
import leoszr.finance_app.transaction.repository.TransactionRepository;
import leoszr.finance_app.user.entity.User;
import leoszr.finance_app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TransactionServiceTest {
	@Mock TransactionRepository transactions; @Mock CategoryRepository categories; @Mock UserRepository users; @Mock leoszr.finance_app.savingbox.repository.SavingBoxRepository boxes; @InjectMocks TransactionService service;
	UUID userId=UUID.randomUUID(); User user=new User();
	@Test void createCommonTransactionUsesUncategorizedWhenNoCategory(){ Category c=cat(CategorySpecialType.UNCATEGORIZED); when(users.findById(userId)).thenReturn(Optional.of(user)); when(categories.findByUserIdAndSpecialType(userId, CategorySpecialType.UNCATEGORIZED)).thenReturn(Optional.of(c)); when(transactions.save(any())).thenAnswer(i->i.getArgument(0)); var res=service.create(userId, req(null, "10.00")); assertThat(res.categorySpecialType()).isEqualTo(CategorySpecialType.UNCATEGORIZED); verify(transactions).save(any()); }
	@Test void rejectsZeroAmount(){ when(users.findById(userId)).thenReturn(Optional.of(user)); assertThatThrownBy(() -> service.create(userId, req(null, "0.00"))).isInstanceOf(BusinessRuleException.class); }
	@Test void getByIdOnlyCurrentUser(){ UUID id=UUID.randomUUID(); when(transactions.findByIdAndUserId(id,userId)).thenReturn(Optional.empty()); assertThatThrownBy(() -> service.get(userId,id)).isInstanceOf(NotFoundException.class); verify(transactions).findByIdAndUserId(id,userId); }
	@Test void updateRejectsSavedCategoryWithoutBox(){ UUID id=UUID.randomUUID(); Transaction t=new Transaction(); Category saved=cat(CategorySpecialType.SAVED); when(transactions.findByIdAndUserId(id,userId)).thenReturn(Optional.of(t)); when(categories.findByIdAndUserId(saved.getId(), userId)).thenReturn(Optional.of(saved)); assertThatThrownBy(() -> service.update(userId,id,req(saved.getId(),"1.00"))).isInstanceOf(BusinessRuleException.class); }
	@Test void deleteUsesUserScopedLookup(){ UUID id=UUID.randomUUID(); Transaction t=new Transaction(); when(transactions.findByIdAndUserId(id,userId)).thenReturn(Optional.of(t)); service.delete(userId,id); verify(transactions).delete(t); }
	private TransactionRequest req(UUID categoryId, String amount){ return new TransactionRequest(TransactionType.EXPENSE,new BigDecimal(amount), LocalDate.now(),categoryId,null,"Desc"); }
	private Category cat(CategorySpecialType s){ Category c=new Category(); try { var f=Category.class.getDeclaredField("id"); f.setAccessible(true); f.set(c, UUID.randomUUID()); } catch(Exception ignored){} c.setName("Cat"); c.setSpecialType(s); return c; }
}
