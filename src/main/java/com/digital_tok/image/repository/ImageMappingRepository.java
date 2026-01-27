package com.digital_tok.image.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.digital_tok.image.domain.Image;
import com.digital_tok.image.domain.ImageMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMappingRepository extends JpaRepository<ImageMapping, Long> {

    Optional<ImageMapping> findByUserIdAndImage(Long userId, Image image);

    Optional<ImageMapping> findByUserIdAndImage_ImageId(Long userId, Long imageId);
    Page<ImageMapping>
    findByUserIdAndLastUsedAtIsNotNullAndImage_DeletedAtIsNullOrderByLastUsedAtDesc(
            Long userId,
            Pageable pageable
    );

}
