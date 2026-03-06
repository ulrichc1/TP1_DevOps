/**
 * INGE2APP LSI1
 * Date : 06/03/2026
 */
package com.ulrich.dockerjavaspringboot.repository;

import com.ulrich.dockerjavaspringboot.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
