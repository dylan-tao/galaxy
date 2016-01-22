/*
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 javaosc.com Team. All Rights Reserved.
 */
$(function() {
	var w_height = $(window).height();
	var m_height = w_height - 95 - 30;
	$(".leftmain").css('height',m_height+'px');
	$(".rightmainbox").css('height',m_height+'px');
	$(".but").css('height',m_height+'px');
	
	$(".but").toggle(
	  function () {
		$(".leftmenu").hide();
		$(".rightmain").css('padding-left','12px');
		$(this).css('background-image','url(../back/images/open.gif)');
	  },
	  function () {
		$(".leftmenu").show();
		$(".rightmain").css('padding-left','212px');
		$(this).css('background-image','url(../back/images/close.gif)');
	  }
	);

    var select_li_index = 0;
	$(".nav li").click(function(){  
	   select_li_index = $(this).index();
       $(this).addClass("content").find("a").addClass("content");
       $(this).siblings().removeClass("content").find("a").removeClass("content");
	   var left_menu = $(".leftmenu dl:eq("+select_li_index+")");
	   $(left_menu).find("dd").first().addClass("content").siblings().removeClass("content");
	   $(left_menu).show().siblings().hide();
	   
	   top_menu_load(select_li_index);
	});  

	 $(".leftmenu dd").click(function(){  
       $(this).addClass("content").siblings().removeClass("content");
	});  
	 
	function top_menu_load(index){
		if(index==2){
			load("/user/list","");
		}
	}
	 
	function load(url,token){
		url = url + "?token=" + token;
		$.get(url,function(data){
			console.log(data);
			$("#right_main").empty().append(data);
		});
	}
	
});