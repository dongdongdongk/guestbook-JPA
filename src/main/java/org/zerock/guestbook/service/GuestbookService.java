package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

public interface GuestbookService {
    // 게시글 등록
    Long register(GuestbookDTO dto);
    // 상세 페이지
    GuestbookDTO read(Long gno);
    // 게시글 삭제
    void remove(Long gno);
    // 게시글 수정
    void modify(GuestbookDTO dto);

    // 페이저 엔티티 DTO 리스트로
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    // DTO -> Entity
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    // Entity -> DTO
    default GuestbookDTO entityToDto(Guestbook entity) {
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
