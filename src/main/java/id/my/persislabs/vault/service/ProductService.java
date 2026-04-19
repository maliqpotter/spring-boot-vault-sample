package id.my.persislabs.vault.service;

import id.my.persislabs.vault.exception.ResourceNotFoundException;
import id.my.persislabs.vault.model.Product;
import id.my.persislabs.vault.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-vault-sample
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 19/04/26
 * Time: 10.03
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public Product create(Product product) {
        Instant now = Instant.now();
        product.setId(null);
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return repository.save(product);
    }

    public Product update(Long id, Product input) {
        Product existing = findById(id);
        existing.setName(input.getName());
        existing.setDescription(input.getDescription());
        existing.setPrice(input.getPrice());
        existing.setStock(input.getStock());
        existing.setUpdatedAt(Instant.now());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Product existing = findById(id);
        repository.delete(existing);
    }
}
