package com.navv.Dto;

import com.navv.model.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private List<Feedback> feedbackList;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElement;
    private Integer totalPages;
    private boolean lastPage;

}
