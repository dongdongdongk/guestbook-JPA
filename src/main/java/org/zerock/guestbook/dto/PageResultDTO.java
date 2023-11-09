package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private  List<Integer> pageList;

    // 생성자: Page<EN> 타입의 데이터와 매핑 함수(Function<EN, DTO>)를 받음
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        // result 내의 엔티티 데이터를 DTO로 변환하여 리스트로 저장
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        // 총 페이지 수 설정
        totalPage = result.getTotalPages();
        // 페이지 번호 목록 생성
        makePageList(result.getPageable());

    }

    // 현재 페이지 정보를 기반으로 페이지 번호 목록 생성
    public void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        // 페이지 번호 목록 생성 (start부터 end까지)
        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
        // boxed() 는 IntStream 을 Integer 로 변환하여 List<Integer> pageList 에 수집 가능하도록 래퍼 객체로 바꿔준다
    }
}
