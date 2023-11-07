package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    //더미 데이터 인서트 테스트
    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title....." + i)
                    .content("Content...." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });

    }

    //데이터를 변경해서 수정시간이 등록되는지 확인
    @Test
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L); //존재하는 번호로 테스트
        if(result.isPresent()) {
            Guestbook guestbook = result.get();
            guestbook.changeTitle("Changed Title......");
            guestbook.changeContent("Changed Content.....");
            guestbookRepository.save(guestbook);
        }
    }

    @Test
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        //fidAll 은 JpaRepository 에 있는 메서드인데 거기에 Querydsl 을 적용하려면 QuerydslPredicateExecutor<> 를 사용한다

        //Predicate 는 자바에도 있기 때문에 임포트 할때 주의
        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //title like %1% 같은걸 코드를 통해서 만들어 내는것이라 생각하면 편하다
        BooleanExpression expression = qGuestbook.title.contains("1");

        //익스프레션을 빌더에 추가해서 조건을 추가해준다
        booleanBuilder.and(expression);

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder,pageable);

        result.forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
    @Test
    public void testQuery2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // title like %1% or content like %1%
        BooleanExpression expression = qGuestbook.title.contains("1");
        BooleanExpression exAll = expression.or(qGuestbook.content.contains("1"));

        booleanBuilder.and(exAll);
        booleanBuilder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        result.forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
}
