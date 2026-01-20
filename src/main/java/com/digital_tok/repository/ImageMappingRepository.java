package com.digital_tok.repository;

import com.digital_tok.domain.Image;
import com.digital_tok.domain.ImageMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageMappingRepository extends JpaRepository<ImageMapping, Long> {

    Optional<ImageMapping> findByUserIdAndImage(Long userId, Image image);

    Optional<ImageMapping> findByUserIdAndImage_ImageId(Long userId, Long imageId);
}
