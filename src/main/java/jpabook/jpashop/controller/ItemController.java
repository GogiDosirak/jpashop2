package jpabook.jpashop.controller;

import jpabook.jpashop.Service.ItemService;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";

    }

    // items/1/edit 이런식으로 들어가지만, 중간 숫자는 변경될 수 있으므로
    // PathVariable 사용
    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model ) {
        Book item = (Book) itemService.findOne(itemId);

        // 업데이트에 엔티티가 아니라 BookForm을 보낼 것
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
        // 수정창이면 데이터가 보여야하므로 일단 GetMapping에선 form으로 데이터를 넘겨줌
    }

    // @ModelAttribute("이름") 해주면 html에서 저 이름으로 넘긴 데이터를 받아옴
    // @PathVariable은 여기서 데이터를 사용하지 않으므로 안써도됨
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form, Model model ) {

        // 받아온 Form을 다시 Book으로 바꿔줘야함
        Book book = new Book();
        book.setId(form.getId());
        // 실무에선 유저가 id를 바꿔서 맘대로 수정할 수 없게끔, item에 대해 권한이 있는지 없는지 체크하는 로직이 서버에 존재해야함!
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        // 그리고 다시 저장
        itemService.saveItem(book);

        return "redirect:/items";

    }

}
