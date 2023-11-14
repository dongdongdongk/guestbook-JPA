package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; //반드시 final 로 선언

    // 게시글 상세
    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result  = repository.findById(gno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }
    // 게시글 삭제
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }
    // 게시글 수정
    @Override
    public void modify(GuestbookDTO dto) {
        // 업데이트 항목 제목, 내용
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if(result.isPresent()) {
            // result.get()은 Optional 객체 안에 실제로 값이 존재한다면 그 값을 반환,
            // 값이 없는 경우에는 NoSuchElementException을 발생
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }
    // 게시글 등록
    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO--------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);
        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }


    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리 getSearch 를 하면 BooleanBuilder 가 나올거고

        Page<Guestbook> result = repository.findAll(booleanBuilder,pageable); //findAll 을 할 때 booleanBuilder 을 이용해서 Querydsl 사용

//        Page<Guestbook> result = repository.findAll(pageable); // 과거 버전

        Function<Guestbook, GuestbookDTO> fn = (entity ->
                entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }


    private BooleanBuilder getSearch(PageRequestDTO requestDTO) { // BooleanBuilder 를 리턴 해준다

        //타입 조건 처리
        String type = requestDTO.getType(); // 타입 정보 얻어오기 (제목,작성자,내용)

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // 불리언 빌더 생성

        if(type == null || type.trim().length() == 0) { // 검색 조건(타입)이 없는 경우
            return booleanBuilder;
        }

        QGuestbook qGuestbook = QGuestbook.guestbook; // Q도메인 객체

        String keyword = requestDTO.getKeyword(); // 키워드 받아오기

        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건만 생성

        booleanBuilder.and(expression); // and 로 추가


        // 검색 조건 작성 (or or 로 추가해서 작성)
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword)); // or or 로 계속 추가중
        }
        if (type.contains("c")) {
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합 ( conditionBuilder 가 booleanBuilder 안에 담기고 위의  .and(expression)(검색 타입) 가 추가된다 )
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}