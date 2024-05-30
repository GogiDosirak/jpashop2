package jpabook.jpashop.Service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 이게 있어야 스프링부트에서 테스트됨
@Transactional // 데이터를 변경할 것이기 떄문에 + 이게 있어야 롤백이 됨(테스트에서만)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(value = false) // 얘를 적어주면 commit까지 해줌
    public void 회원가입() throws Exception {
        //given (어떤게 주어졌을때)
        Member member = new Member();
        member.setName("kim");

        //when (어떤걸 실행하면)
        Long saveId = memberService.join(member);

        //then (어떤 결과가 나오는가)
        assertEquals(member, memberRepository.findOne(saveId));
        //멤버와 Repository에서 찾아온 멤버가 똑같으면 잘 저장된 것
        //Why? @Transactional 덕분, JPA에서 같은 트랜젝션 안에서 PK값이 같으면 같은 엔티티임
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
        // JUnit5에선 예외가 발생해야 테스트가 성공하므로,
        // fail을 작성해선 안됨



    }

}