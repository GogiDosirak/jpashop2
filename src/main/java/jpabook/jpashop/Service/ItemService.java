package jpabook.jpashop.Service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    // 물품 등록
    @Transactional // 기본이 readOnly 이므로 이게 필요함
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 단건 조회
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    // 여러건 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }



}
