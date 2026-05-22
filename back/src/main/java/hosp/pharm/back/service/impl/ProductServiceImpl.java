package hosp.pharm.back.service.impl;

import hosp.pharm.back.exception.EntityNotFoundException;
import hosp.pharm.back.exception.NullIdentifierException;
import hosp.pharm.back.filter.ProductFilter;
import hosp.pharm.back.mapper.ProductMapper;
import hosp.pharm.back.model.dto.create.ProductCreateDto;
import hosp.pharm.back.model.dto.response.ProductResponseDto;
import hosp.pharm.back.model.dto.update.ProductUpdateDto;
import hosp.pharm.back.model.entity.CountryEntity;
import hosp.pharm.back.model.entity.ProductEntity;
import hosp.pharm.back.dao.repository.CountryRepository;
import hosp.pharm.back.dao.selector.ProductQuerySelector;
import hosp.pharm.back.dao.repository.ProductRepository;
import hosp.pharm.back.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CountryRepository countryRepository;

    private final ProductQuerySelector querySelector;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Page<ProductResponseDto> getAll(final ProductFilter filter, final Pageable pageable) {
        final Page<ProductEntity> entities = querySelector.getByDynamicFilter(filter, pageable);
        final List<ProductResponseDto> dtos = entities.get().map(productMapper::toDto).toList();
        long totalElements = entities.getTotalElements();

        return new PageImpl<>(dtos, pageable, totalElements);
    }

    @Override
    public ProductResponseDto getById(final Long id) {
        final ProductEntity entity = getProductById(id);

        return productMapper.toDto(entity);
    }

    @Override
    public ProductResponseDto create(final ProductCreateDto dto) {
        final ProductEntity entity = productMapper.toEntity(dto);
        setCountryOfOrigin(entity, dto.getCountryId());

        final ProductEntity persisted = productRepository.save(entity);

        return productMapper.toDto(persisted);
    }

    @Override
    public ProductResponseDto update(final ProductUpdateDto dto) {
        final ProductEntity entity = getProductById(dto.getId());
        setCountryOfOrigin(entity, dto.getCountryId());

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getProductType() != null) entity.setType(dto.getProductType());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getImageId() != null) entity.setImageId(dto.getImageId());
        if (dto.getIsRequiredRecipe() != null) entity.setIsRequiredRecipe(dto.getIsRequiredRecipe());
        if (dto.getManufacturer() != null) entity.setManufacturer(dto.getManufacturer());

        final ProductEntity persisted = productRepository.save(entity);

        return productMapper.toDto(persisted);
    }

    @Override
    @Transactional
    public void disable(final Long id) {
        final ProductEntity entity = getProductById(id);

        entity.setActive(false);
        entity.getBatches().forEach(batch -> batch.setActive(false));

        productRepository.save(entity);
    }

    private ProductEntity getProductById(final Long id) {
        if(id == null) throw new NullIdentifierException();

        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private void setCountryOfOrigin(final ProductEntity entity, final Long countryId) {
        if(countryId != null) {
            final CountryEntity country = countryRepository.findById(countryId)
                    .orElseThrow(EntityNotFoundException::new);

            entity.setCountryOfOrigin(country);
        }
    }

}
