package com.codessquad.qna;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {
    private static final int INITIAL_PAGE_NUMBER = 0;
    private static final int QUESTIONS_OF_EACH_PAGE = 4;
    private static final int SIZE_OF_PAGE_BAR = 5;
    private int firstPage;
    private int lastPage;
    private int totalPage;

    private List<Page> pages;

    public PageUtils(int firstPage, int lastPage) {
        this.firstPage = firstPage;
        this.lastPage = lastPage;
    }

    public void initPage(QuestionRepository questionRepository) {
        Page page = createPage(INITIAL_PAGE_NUMBER, questionRepository);
        totalPage = page.getTotalPages();
        if (totalPage < SIZE_OF_PAGE_BAR) {
            lastPage = totalPage + 1;
        }
    }

    public Page createPage(int pageIndex, QuestionRepository questionRepository) {
        PageRequest pageRequest = PageRequest.of(pageIndex, QUESTIONS_OF_EACH_PAGE, Sort.by("postingTime").descending());
        Page page = questionRepository.findAllByDeletedFalse(pageRequest);
        return page;
    }

    public List<Page> createPages(QuestionRepository questionRepository) {
        Page page;
        List<Page> pages = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            page = createPage(i, questionRepository);
            pages.add(page);
        }
        this.pages = pages;
        return pages;
    }

    public List<Page> getSubPages(QuestionRepository questionRepository) {
        return isLastPageBar() ? getLastSubPages(pages, questionRepository) : getRegularSubPages(pages);
    }

    public boolean isLastPageBar() {
        return lastPage > totalPage;
    }

    public boolean isFirstPageBar() {
        return firstPage == 1;
    }

    public List<Page> getRegularSubPages(List<Page> inputPages) {
        return inputPages.subList(firstPage, lastPage);
    }

    public List<Page> getLastSubPages(List<Page> inputPages, QuestionRepository questionRepository) {
        List<Page> pages = inputPages.subList(firstPage, lastPage - 1);
        pages.add(createPage(lastPage - 1, questionRepository));
        return pages;
    }

    public int plusPageCount() {
        firstPage += SIZE_OF_PAGE_BAR;
        lastPage += SIZE_OF_PAGE_BAR;

        if (firstPage > totalPage) {
            firstPage -= SIZE_OF_PAGE_BAR;
        }
        if (lastPage > totalPage) {
            lastPage = totalPage + 1;
        }
        return firstPage;
    }

    public int minusPageCount() {
        if (totalPage == lastPage - 1) {
            lastPage += (SIZE_OF_PAGE_BAR - (lastPage - firstPage));
        }
        firstPage -= SIZE_OF_PAGE_BAR;
        lastPage -= SIZE_OF_PAGE_BAR;

        return firstPage;
    }

    public String preButton() {
        if (!isFirstPageBar()) return " ";
        return null;
    }

    public String nextButton() {
        if (!isLastPageBar()) return " ";
        return null;
    }
}
