package com.petconsultant.repository;

import com.petconsultant.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByIsActiveTrue();
    List<ProductEntity> findByCategoryIdAndIsActiveTrue(Long categoryId);
    List<ProductEntity> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
}
