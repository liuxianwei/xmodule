/**
 * 在浏览器请求页面开始,到Vue编译完整个页面文档这段短暂的时间内,页面样式会出现短暂的页面样式怪异抖动现象(究其原因是在这短暂时间内浏览器不认识Vue自定义组件标签,浏览器无法渲染页面及样式导致的)
 * 本插件即用来优化该体验问题,即：整个暂时hidden，然后再fade
 */

var pagerInner = document.querySelector(".el-page-inner");
var FULL_SCREEN_LOADING = null;
/**
 * 插件
 */
Vue.mixin({
	created: function(){
		if (this.$options.el == '#app') { //当插件应用在当前app页面时才执行removeClass
			if(!this._data.diablePageLoading){
				FULL_SCREEN_LOADING = this.$loading({fullscreen: true, spinner: 'el-loading-pulse-v'}); //开启整页loading效果
			}
			removeClass(pagerInner, 'el-hidden');
		}
	}
})
