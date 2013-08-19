package com.haks.haksvn.source.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haks.haksvn.common.message.model.ResultMessage;
import com.haks.haksvn.common.security.util.ContextHolder;
import com.haks.haksvn.source.model.Review;
import com.haks.haksvn.source.model.ReviewComment;
import com.haks.haksvn.source.model.ReviewId;
import com.haks.haksvn.source.service.ReviewService;
import com.haks.haksvn.user.service.UserService;

@Controller
@RequestMapping(value="/source/review")
public class ReviewAjaxController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private UserService userService;
    
	@RequestMapping(value={"/{repositoryKey}/{revision}"}, method=RequestMethod.POST)
    public @ResponseBody ResultMessage saveReview(@PathVariable String repositoryKey,
    												@PathVariable long revision,
    												@ModelAttribute("review") Review review) throws Exception{
		ResultMessage message = new ResultMessage("Save Review success");
		reviewService.saveReview(Review.Builder.getBuilder(review).reviewId(ReviewId.Builder.getBuilder().repositoryKey(repositoryKey).revision(revision)
					.reviewer(userService.retrieveUserByUserId(ContextHolder.getLoginUser().getUserId())).build()).build());
		return message;
    }
	
	@RequestMapping(value={"/{repositoryKey}/{revision}/comment/{reviewCommentSeq}"}, method=RequestMethod.DELETE)
    public @ResponseBody ResultMessage saveReview(@PathVariable String repositoryKey,
    												@PathVariable long revision,
    												@PathVariable int reviewCommentSeq) throws Exception{
		ResultMessage message = new ResultMessage("Review comment deleted");
		reviewService.deleteReviewComment(ReviewComment.Builder.getBuilder().reviewCommentSeq(reviewCommentSeq).build());
		return message;
    }
	
	@RequestMapping(value="/{repositoryKey}/{revision}/request", method=RequestMethod.POST)
    public @ResponseBody ResultMessage addRepositoryUser(@PathVariable String repositoryKey,
    												@PathVariable long revision,
    												@RequestParam(value = "userId", required = true) String[] userIdList){
    	
    	ResultMessage message = new ResultMessage("send mail success");
    	reviewService.requestReview(ReviewId.Builder.getBuilder().repositoryKey(repositoryKey).revision(revision).build(), userIdList);
    	return message;
    }
	
	@ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e, HttpServletResponse response) throws Exception{
    	e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(e.getMessage());
        response.flushBuffer();
    }
}
