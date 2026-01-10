package com.github.cauebf.shoppingcartapi.service.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.exceptions.AlreadyExistsException;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Category;
import com.github.cauebf.shoppingcartapi.repository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        // construtor dependency injection
        this.categoryRepository = categoryRepository; 
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category) 
                .filter(c -> !categoryRepository.existsByName(c.getName())) // check if the category is already in the DB
                .map(categoryRepository::save) // if not, save it
                .orElseThrow(() -> new AlreadyExistsException(category.getName() + " already exists!")); // if yes, throw exception
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    // if found, update
                    oldCategory.setName(category.getName()); // update the name
                    return categoryRepository.save(oldCategory); // save the updated category
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!")); // if not found, throw exception
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new ResourceNotFoundException("Category not found!");
        });
    }
    
}
