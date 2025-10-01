package com.petfriendly.backend.dto.mapper;

import com.petfriendly.backend.dto.response.*;
import com.petfriendly.backend.entity.*;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;

public final class DtoMapper {

    private DtoMapper() {}

    public static UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCity(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static FoundationResponse toFoundationResponse(Foundation foundation) {
        if (foundation == null) {
            return null;
        }
        return new FoundationResponse(
                foundation.getId(),
                foundation.getName(),
                foundation.getCity(),
                foundation.getState(),
                foundation.getDescription(),
                foundation.getContactEmail(),
                foundation.getWebsite(),
                foundation.getAddress(),
                foundation.getPhoneNumber(),
                Boolean.TRUE.equals(foundation.getVerified()),
                foundation.getCreatedAt(),
                foundation.getUpdatedAt()
        );
    }

    public static PetResponse toPetResponse(Pet pet) {
        if (pet == null) {
            return null;
        }
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getAge(),
                pet.getGender(),
                pet.getSize(),
                pet.getDescription(),
                pet.getStatus(),
                pet.getFoundation() != null ? pet.getFoundation().getId() : null,
                pet.getCreatedAt(),
                pet.getUpdatedAt()
        );
    }

    public static PetImageResponse toPetImageResponse(PetImage petImage) {
        if (petImage == null) {
            return null;
        }
        return new PetImageResponse(
                petImage.getId(),
                petImage.getImageUrl(),
                Boolean.TRUE.equals(petImage.getIsPrimary()),
                petImage.getAltText(),
                petImage.getPet() != null ? petImage.getPet().getId() : null,
                petImage.getCreatedAt(),
                petImage.getUpdatedAt()
        );
    }

    public static AdoptionRequestResponse toAdoptionRequestResponse(AdoptionRequest adoptionRequest) {
        if (adoptionRequest == null) {
            return null;
        }
        return new AdoptionRequestResponse(
                adoptionRequest.getId(),
                adoptionRequest.getUser() != null ? adoptionRequest.getUser().getId() : null,
                adoptionRequest.getPet() != null ? adoptionRequest.getPet().getId() : null,
                adoptionRequest.getMessage(),
                adoptionRequest.getExperience(),
                adoptionRequest.getLivingSituation(),
                adoptionRequest.getReviewNotes(),
                adoptionRequest.getStatus(),
                adoptionRequest.getCreatedAt(),
                adoptionRequest.getUpdatedAt(),
                adoptionRequest.getReviewedAt()
        );
    }

    public static ContactMessageResponse toContactMessageResponse(ContactMessage message) {
        if (message == null) {
            return null;
        }
        return new ContactMessageResponse(
                message.getId(),
                message.getFoundation() != null ? message.getFoundation().getId() : null,
                message.getSenderName(),
                message.getSenderEmail(),
                message.getSubject(),
                message.getMessage(),
                Boolean.TRUE.equals(message.getIsRead()),
                message.getCreatedAt(),
                message.getReadAt()
        );
    }

    public static <T> List<T> nonNullList(List<T> list) {
        return list == null ? List.of() : list.stream().filter(Objects::nonNull).toList();
    }

    public static List<UserResponse> toUserResponses(List<User> users) {
        return nonNullList(users).stream().map(DtoMapper::toUserResponse).toList();
    }

    public static List<FoundationResponse> toFoundationResponses(List<Foundation> foundations) {
        return nonNullList(foundations).stream().map(DtoMapper::toFoundationResponse).toList();
    }

    public static List<PetResponse> toPetResponses(List<Pet> pets) {
        return nonNullList(pets).stream().map(DtoMapper::toPetResponse).toList();
    }

    public static List<PetImageResponse> toPetImageResponses(List<PetImage> images) {
        return nonNullList(images).stream().map(DtoMapper::toPetImageResponse).toList();
    }

    public static List<AdoptionRequestResponse> toAdoptionRequestResponses(List<AdoptionRequest> requests) {
        return nonNullList(requests).stream().map(DtoMapper::toAdoptionRequestResponse).toList();
    }

    public static List<ContactMessageResponse> toContactMessageResponses(List<ContactMessage> messages) {
        return nonNullList(messages).stream().map(DtoMapper::toContactMessageResponse).toList();
    }

    public static <S, T> Page<T> mapPage(Page<S> page, java.util.function.Function<S, T> mapper) {
        return page == null ? Page.empty() : page.map(mapper);
    }
}
