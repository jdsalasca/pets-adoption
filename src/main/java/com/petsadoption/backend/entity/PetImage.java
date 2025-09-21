package com.petsadoption.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una imagen de una mascota.
 * Las imágenes se almacenan en S3 y se referencian por su clave.
 */
@Entity
@Table(name = "pet_images", indexes = {
    @Index(name = "idx_pet_images_pet_id", columnList = "pet_id"),
    @Index(name = "idx_pet_images_order", columnList = "pet_id, order_index")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @ToString.Exclude
    private Pet pet;

    @NotBlank(message = "La clave S3 es obligatoria")
    @Size(max = 500, message = "La clave S3 no puede exceder 500 caracteres")
    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @NotNull(message = "El índice de orden es obligatorio")
    @Min(value = 0, message = "El índice de orden debe ser mayor o igual a 0")
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Size(max = 255, message = "El texto alternativo no puede exceder 255 caracteres")
    @Column(name = "alt_text")
    private String altText;

    @Column(name = "is_primary", nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "file_size")
    private Long fileSize;

    @Size(max = 50, message = "El tipo de archivo no puede exceder 50 caracteres")
    @Column(name = "content_type")
    private String contentType;

    @Size(max = 255, message = "El nombre original no puede exceder 255 caracteres")
    @Column(name = "original_filename")
    private String originalFilename;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Método de conveniencia para obtener la URL completa de la imagen.
     * Nota: En un entorno real, esto debería construirse usando la configuración de S3.
     */
    public String getImageUrl() {
        if (s3Key == null || s3Key.isEmpty()) {
            return null;
        }
        // TODO: Implementar construcción de URL usando configuración de S3
        return "https://your-bucket.s3.amazonaws.com/" + s3Key;
    }

    /**
     * Método de conveniencia para verificar si es la imagen principal.
     */
    public boolean isPrimaryImage() {
        return Boolean.TRUE.equals(isPrimary);
    }

    /**
     * Método de conveniencia para obtener el tamaño del archivo en formato legible.
     */
    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "Desconocido";
        }
        
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        } else {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        }
    }

    /**
     * Método de conveniencia para verificar si es una imagen válida.
     */
    public boolean isValidImageType() {
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("image/") && 
               (contentType.equals("image/jpeg") || 
                contentType.equals("image/png") || 
                contentType.equals("image/webp") ||
                contentType.equals("image/gif"));
    }

    // Getters y setters manuales
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}