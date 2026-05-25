package leoszr.finance_app.category.controller;

import jakarta.validation.Valid;
import leoszr.finance_app.category.dto.*;
import leoszr.finance_app.category.service.CategoryService;
import leoszr.finance_app.security.CurrentUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	private final CurrentUserProvider currentUserProvider; private final CategoryService categoryService;
	public CategoryController(CurrentUserProvider currentUserProvider, CategoryService categoryService) { this.currentUserProvider = currentUserProvider; this.categoryService = categoryService; }
	@GetMapping public List<CategoryResponse> list() { return categoryService.list(currentUserProvider.currentUser().userId()); }
	@PostMapping @ResponseStatus(HttpStatus.CREATED) public CategoryResponse create(@Valid @RequestBody CategoryRequest request) { return categoryService.create(currentUserProvider.currentUser().userId(), request); }
	@PatchMapping("/{id}") public CategoryResponse update(@PathVariable UUID id, @Valid @RequestBody CategoryRequest request) { return categoryService.update(currentUserProvider.currentUser().userId(), id, request); }
	@DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable UUID id) { categoryService.delete(currentUserProvider.currentUser().userId(), id); }
}
