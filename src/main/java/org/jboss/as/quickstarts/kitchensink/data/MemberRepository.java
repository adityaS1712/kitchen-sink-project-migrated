/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.data;


import java.util.List;
import java.util.Optional;


import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByOrderByNameAsc();
    Optional<Member> findById(Long id);
   Optional<Member> findByEmail(String email);



//    @Autowired
//    private EntityManager em;
//
//    public Member findById(Long id) {
//        return em.find(Member.class, id);
//    }
//
//    public Member findByEmail(String email) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
//        Root<Member> member = criteria.from(Member.class);
//        criteria.select(member).where(cb.equal(member.get("email"), email));
//        return em.createQuery(criteria).getSingleResult();
//    }
//
//    public List<Member> findAllOrderedByName() {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
//        Root<Member> member = criteria.from(Member.class);
//        criteria.select(member).orderBy(cb.asc(member.get("name")));
//        return em.createQuery(criteria).getResultList();
//    }
}
