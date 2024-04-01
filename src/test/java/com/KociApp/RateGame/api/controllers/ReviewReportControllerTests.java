package com.KociApp.RateGame.api.controllers;

import com.KociApp.RateGame.review.*;
import com.KociApp.RateGame.review.reports.*;
import com.KociApp.RateGame.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewReportController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ReviewReportControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ReviewReportService reviewReportService;


    @Test
    public void getReportsTest() throws Exception {

        List<ReviewReport> reviewReports = new ArrayList<>();
        reviewReports.add(new ReviewReport(1L, "test", new User(), new Review()));
        reviewReports.add(new ReviewReport(2L, "test", new User(), new Review()));

        when(reviewReportService.findAll()).thenReturn(reviewReports);

        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewReports)));
    }

    @Test
    public void getReportByIdTest() throws Exception {

        ReviewReport reviewReport = new ReviewReport(1L, "test", new User(), new Review());

        when(reviewReportService.findById(1L)).thenReturn(reviewReport);

        mockMvc.perform(get("/reports/byId/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewReport)));
    }

    @Test
    public void createReportTest() throws Exception {

        ReviewReportRequest reviewReportRequest = new ReviewReportRequest("test", 1L);
        ReviewReport reviewReport = new ReviewReport(1L, "test", new User(), new Review());

        when(reviewReportService.save(reviewReportRequest)).thenReturn(reviewReport);

        mockMvc.perform(post("/reports").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reviewReportRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewReport)));
    }

    @Test
    public void raportTest() throws Exception {

        List<ReviewReportRaport> reviewReportRaports = new ArrayList<>();
        reviewReportRaports.add(new ReviewReportRaport(1L, "test", 5, 1L));
        reviewReportRaports.add(new ReviewReportRaport(2L, "test", 5, 2L));

        when(reviewReportService.raport()).thenReturn(reviewReportRaports);

        mockMvc.perform(get("/reports/raport"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewReportRaports)));
    }
}
