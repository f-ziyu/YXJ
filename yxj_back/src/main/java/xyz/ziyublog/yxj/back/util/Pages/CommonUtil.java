package xyz.ziyublog.yxj.back.util.Pages;

public class CommonUtil {
    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        int pageNum = start / count;
        return PageCounter.builder().page(pageNum).count(count).build();
    }
}
