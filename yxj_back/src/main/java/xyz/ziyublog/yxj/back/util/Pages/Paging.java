package xyz.ziyublog.yxj.back.util.Pages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {
    private Long total;         // 查询内容总数量
    private Integer count;      // 每页显示的数量
    private Integer page;       // 页码
    private Integer totalPage;  // 总页数
    private List<T> items;      // 每页显示的内容列表

    public Paging(Page<T> pageT) {
        this.initPageParameters(pageT);
        this.items = pageT.getContent();
    }

    void initPageParameters(Page<T> pageT) {
        this.total = pageT.getTotalElements();
        this.count = pageT.getSize();
        this.page = pageT.getNumber();
        this.totalPage = pageT.getTotalPages();
    }
}