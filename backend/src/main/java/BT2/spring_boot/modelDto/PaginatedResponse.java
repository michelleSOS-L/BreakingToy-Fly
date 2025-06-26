package BT2.spring_boot.modelDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private int totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public PaginatedResponse(List<T> pagedData, int totalItems, int currentPage, int pageSize) {
        this.data = pagedData;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }
}

