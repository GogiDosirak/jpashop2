package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // 저장하는 로직
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // id값으로 멤버를 찾아서 반환하는 로직 (단건조회)
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); // 멤버 전체를 반환하는 로직, JPQL 사용 (엔티티 객체를 대상으로 쿼리)
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class) // :name 해줘야 파라미터 바인딩
                .setParameter("name", name) // 여기서 파라미터 바인딩 해주고,
                .getResultList();  // 이름으로 회원 조회
    }




}
