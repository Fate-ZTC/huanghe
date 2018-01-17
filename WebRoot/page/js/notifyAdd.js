KindEditor.ready(function(K) {
	var editor = K.editor({
		allowFileManager : true
	});
	//富文本编辑器
	K.create('textarea', {
//			allowFileManager : true,
//			langType : 'zh-CN',
			autoHeightMode : false,
			filterMode : false,
		wellFormatMode : false,

		items : [
	        'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
	        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
	        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
	        'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
	        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
	        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
	        'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
	        'anchor', 'link', 'unlink'
		],
		afterBlur: function(){this.sync();}
	})
});
