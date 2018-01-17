package com.system.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 框架
 * @author LH
 * @since 1.0
 */
@Controller
public class FrameController{

	private static final long serialVersionUID = 1031353529611997183L;
	@RequestMapping("frame_footer")
	public ModelAndView footer()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/footer");
		return mv;
	}
	@RequestMapping("frame_top")
	public ModelAndView top()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/top");
		return mv;
	}
	@RequestMapping("frame_left")
	public ModelAndView left()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("manager/left");
		return mv;
	}

}
