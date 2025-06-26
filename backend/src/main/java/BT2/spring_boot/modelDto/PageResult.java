package BT2.spring_boot.modelDto;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
