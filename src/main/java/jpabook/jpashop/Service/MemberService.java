package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // JPA에서 데이터변경은 트랜잭션 안에서 일어나야함, 필수!, 클래스 위에 해주면 모든 메소드에 트랜젝션이 걸려 들어감
// (스프링에서 제공하는 트랜젝션을 사용하는게 좋음)
// 이걸 붙여주면 조회하는 곳에서 JPA가 성능을 최적화함 (읽기 전용)
// 읽기엔 가급적 이걸 넣어주고, 데이터 변경이 필요한 곳엔 넣지말자
// 여긴 읽기가 더 많으므로 이걸 베이스로 깔고, 변경이 필요한 곳엔 @Transactional만 붙여주면 됨
// @AllArgsConstructor //롬복의 기능, 모든 필드의 생성자를 만들어줌
@RequiredArgsConstructor // final이 있는 애들만 생성자를 만들어줌, 얘를 쓰는게 더 나은 방법
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입 (중복 회원 검증 로직 포함)
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 문제가 있으면 Exception으로 던져주기
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // 하지만 이렇게 해주면, 동시에 가입한 경우엔 둘다 save 될수도 있음 -> 실무에선 DB Name 컬럼에 유니크 제약조건 걸어주는게 안전
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        // 조회해서 영속성 컨텍스트에 넣어주고, 수정하면 변경감지
        // @Transactional에 의해 문장이 끝날 때 자동으로 flush -> commit됨
    }

}
