package com.example.entregafinal.Repository;

import com.example.entregafinal.Model.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {
}
