/**
 * 应用系统定义
 */  
APP_UPMS_API = {
	CONTEXT_PATH: '/xmodule-upms-api',
	APP_DESCRIPTION: '统一用户权限管理系统后台服务'
};
APP_UPMS_WEB = {
	CONTEXT_PATH: '/xmodule-upms-web',
	APP_DESCRIPTION: '统一用户权限管理系统前台页面'
};

/**
 * 系统管理-首页页面展示的顶部NAV菜单的菜单级别
 */
var TOP_NAV_MENU_LEVEL = 1;
/**
 * 页面顶部NAV菜单的高度
 */
var TOP_NAV_MENU_HEIGHT = 60;
/**
 * 页面左侧菜单的宽度
 */
var ASIDE_MENU_WIDTH = 250;
var ASIDE_MENU_COLLAPSED_WIDTH = TOP_NAV_MENU_HEIGHT;
var ASIDE_MENU_EXPANDED_WIDTH = ASIDE_MENU_WIDTH;
/**
 * 页面左侧菜单的收起/展开的sessionStorage状态key
 */
var ASIDE_MENU_COLLAPSE_STATE_KEY = "ASIDE_MENU_COLLAPSE_STATE";
/**
 * 浏览器窗口高度,宽度
 */
var WINDOW_HEIGHT = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
var WINDOW_WIDTH = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
/**
 * 绑定当前应用的Vue实例，这样方便在非Vue环境下使用
 */
Vue.mixin({
	created: function(){
		if (this.$options.el == '#app') { //当插件应用在当前app页面时
			Vue.$appInstance = this;
		}
	}
});
/**
 * axios全局配置
 */
if(axios){
	axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
	/**
	 * 后端依此标识为异步请求
	 */
	axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	/**
	 * 异步请求响应拦截
	 */
	axios.interceptors.response.use(function (response) {
	    return response;
	}, function (error) {
		var statusCode = error.response.status;
		if(statusCode == 403){ //会话过期
			error.response.status = 200;
			if(Vue.$appInstance){
				Vue.$appInstance.$message.error('对不起，您的会话已过期，请重新登录!');
				setTimeout(function(){
					window.top.location.href = APP_UPMS_WEB.CONTEXT_PATH + '/login.html';
				}, 3000);
			} else {
				window.top.location.href = APP_UPMS_WEB.CONTEXT_PATH + '/login.html';
			}
			return Promise.resolve(error.response);
		}else if(statusCode == 401){ //没有权限访问某个URL
			error.response.status = 200;
			var result = error.response.data;
			if(Vue.$appInstance){
				if(result.data){
					Vue.$appInstance.$message.error('对不起，您没有权限访问该URL(' + result.data + ')!');
				}else{
					Vue.$appInstance.$message.error('对不起，您没有权限访问该URL!');
				}
			} else {
				if(result.data){
					alert('对不起，您没有权限访问该URL(' + result.data + ')!');
				}else{
					alert('对不起，您没有权限访问该URL!');
				}
			}
			return Promise.resolve(error.response);
		}
	});
}

/**
 * ajax加载当前登录者对象
 * @param vm				- 当前Vue实例对象this
 * @param successCallback	- 成功获取到用户对象了
 * @param failureCallback  - 获取用户对象报错了
 */
function loadCurrentLoginUser(vm, successCallback, failureCallback){
	axios.get(APP_UPMS_API.CONTEXT_PATH + '/login/user/current').then(function(response){
		var result = response.data;
		if(result && result.success){
			successCallback.call(vm, result.data);
		}
	}).catch(function(error){
		if(failureCallback){
			failureCallback.call(vm);
		}
	});
};

var pageInner = document.querySelector(".el-page-inner");
if(pageInner){
	pageInner.style.minHeight = WINDOW_HEIGHT + 'px';
}

/**
 * 顶部NAV动态菜单组件
 */
var AdminTopNavMenu = Vue.extend({
	render: function(h){
		var _this = this;
		return h('el-menu', {
			'class': _this.menuClass,
			props: {
				mode: 'horizontal',
				defaultActive: _this.defaultActive,
				router: true
			}
		}, _this.recurisiveLoadSubMenu(h, _this.menuList, TOP_NAV_MENU_LEVEL));
	},
	props: {
		menuClass: {
			type: Array,
			default: ['admin-topnav-menu']
		},
		menuList: {
			type: Array,
			default: []
		},
		defaultActive: {
			type: String,
			default: ''
		}
	},
	methods: {
		/**
		 * 递归处理加载渲染菜单树结构
		 */
		recurisiveLoadSubMenu: function(h, menus, level){
			var elements = [];
		    if(menus && menus.length){
		        menus.forEach(function(menu){
		          	var item = null;
		          	if(menu.subMenuList && menu.subMenuList.length){
		            	item = h('el-submenu', {
		              		props: {
			                	index: menu.menuPath,
			                	popperClass: 'admin-topnav-submenu',
			                	slot: 'title'
			              	}
		            	}, [
		            		h('span', {slot: 'title'}, level > 1 ? [h('i', {'class': [this.formatMenuIcon(menu.menuIcon)]}), menu.menuName] : [menu.menuName]), 
		            		this.recurisiveLoadSubMenu(h, menu.subMenuList, level + 1)
		            	]);
		            	elements.push(item);
		          	}else{
		            	item = h('el-menu-item', {
					    	props: {
					    		index: menu.menuPath
					    	}
		            	}, level > 1 ? [h('i', {'class': [this.formatMenuIcon(menu.menuIcon)]}), h('span', { slot: 'title' }, menu.menuName)] : [h('span', menu.menuName)]);
		            	elements.push(item);
		          	}
		        }, this);
			}
		    return elements;
		},
		formatMenuIcon: function(menuIcon){
			if(menuIcon){
				if(menuIcon.toLowerCase().indexOf('el-icon-') == -1){
					menuIcon = 'el-icon-' + menuIcon;
				}
			}else{
				menuIcon = 'el-icon-fa-circle-o';
			}
			return menuIcon;
		}
	}
});
Vue.component('admin-topnav-menu', AdminTopNavMenu); //注册全局组件

/**
 * 顶部NAV动态菜单组件
 */
var AdminAsideMenu = Vue.extend({
	render: function(h){
		var _this = this;
		return h('el-menu', {
			'class': _this.menuClass,
			props: {
				mode: 'vertical',
				defaultActive: _this.defaultActive,
				collapse: _this.collapse,
				router: true
			},
			on: {
				select: _this.onSelected
			},
		}, _this.recurisiveLoadSubMenu(h, _this.menuList, TOP_NAV_MENU_LEVEL));
	},
	props: {
		menuClass: {
			type: Array,
			default: ['admin-aside-menu']
		},
		menuList: {
			type: Array,
			default: []
		},
		defaultActive: {
			type: String,
			default: ''
		},
		collapse: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		/**
		 * 递归处理加载渲染菜单树结构
		 */
		recurisiveLoadSubMenu: function(h, menus){
			var elements = [];
		    if(menus && menus.length){
		        menus.forEach(function(menu){
		          	var item = null;
		          	if(menu.subMenuList && menu.subMenuList.length){
		            	item = h('el-submenu', {
		              		props: {
			                	index: menu.menuPath,
			                	popperClass: 'admin-aside-submenu',
			                	slot: 'title'
			              	}
		            	}, [
		            		h('div', {slot: 'title'}, [h('i', {'class': [this.formatMenuIcon(menu.menuIcon)]}), h('span', menu.menuName)]), 
		            		this.recurisiveLoadSubMenu(h, menu.subMenuList)
		            	]);
		            	elements.push(item);
		          	}else{
		            	item = h('el-menu-item', {
					    	props: {
					    		index: menu.menuPath
					    	}
		            	}, [h('i', {'class': [this.formatMenuIcon(menu.menuIcon)]}), h('span', { slot: 'title' }, menu.menuName)]);
		            	elements.push(item);
		          	}
		        }, this);
			}
		    return elements;
		},
		formatMenuIcon: function(menuIcon){
			if(menuIcon){
				if(menuIcon.toLowerCase().indexOf('el-icon-') == -1){
					menuIcon = 'el-icon-' + menuIcon;
				}
			}else{
				menuIcon = 'el-icon-fa-circle-o';
			}
			return menuIcon;
		},
		onSelected: function(index, indexPath){
			this.$emit('select', index, indexPath);
		}
	}
});
Vue.component('admin-aside-menu', AdminAsideMenu); //注册全局组件

var AdminContainer = Vue.extend({
	template: '<el-container :style="{height: windowHeight}">'
				+ '<el-header :height="headerHeight">'
					+ '<div class="el-row admin-header" :class="{collapsed: aisdeMenuCollapse}">'
						+ '<h3 class="el-col admin-logo"><a href="javascript:;"><img :src="appLogoUrl"/></a><label>{{appTitle}}</label></h3>'
						+ '<nav class="el-col el-row admin-navbar">'
							+ '<div class="el-col nav-menu">'
								+ '<a class="admin-aside-toggle" v-if="showAside" href="javascript:;" :class="{active: aisdeMenuCollapse}" @click="toggleCollapseAsideMenu"><i class="el-icon-fa-bars"></i></a>'
								+ '<admin-topnav-menu :default-active="topnavMenuActive" :menu-list="topnavMenuList" :menu-class="topnavMenuClass"></admin-aside-menu>'
							+ '</div>'
							+ '<div class="el-col tool-menu">'
								+ '<el-menu class="admin-tool-menu" mode="horizontal">'
									+ '<el-menu-item index="/admin/currentuser" class="avatar" style="width:auto;">'
										+ '<el-dropdown trigger="hover">'
											+ '<a class="admin-dropdownlink-userinfo" href="javascript:;" style="width:auto;padding:0 10px;"><img :src="loginUser.userIconUrl"/>{{loginUser.realName}}</a>'
											+ '<el-dropdown-menu class="admin-dropdownmenu-userinfo" slot="dropdown">'
												+ '<el-dropdown-item>'
													+ '<div class="admin-dropdownpanel-userinfo">'
														+ '<div class="el-row">'
															+ '<div class="el-col el-col-6"><img class="userinfo-avatar" :src="loginUser.userIconUrl"></div>'
															+ '<div class="el-col el-col-18 userinfo">'
																+ '<label class="userinfo-name el-ellipsis">{{loginUser.realName}}</label>'
																+ '<p class="userinfo-roles">{{loginUser.userRoleNames}}</p>'
																+ '<span class="userinfo-llt">上次登录时间：{{loginUser.lastLoginTime}}</span>'
															+ '</div>'
														+ '</div>'
														+ '<div class="el-row" style="margin-top:10px;">'
															+ '<div class="el-col el-col-8 actions"><router-link class="el-button el-button--default profile" tag="button" :to="userProfileUrl"><span>资料</span></router-link></div>'
															+ '<div class="el-col el-col-8 actions"><router-link class="el-button el-button--default settings" tag="button" :to="userSettingsUrl"><span>设置</span></router-link></div>'
															+ '<div class="el-col el-col-8 actions"><router-link class="el-button el-button--default logout" tag="button" :to="logoutUrl"><span>退出</span></router-link></div>'
														+ '</div>'
													+ '</div>'
												+ '</el-dropdown-item>'
											+ '</el-dropdown-menu>'
										+ '</el-dropdown>'
									+ '</el-menu-item>'
									+ '<el-menu-item index="/admin/logout">'
										+ '<router-link :to="logoutUrl"><i class="el-icon-fa-power-off" title="退出"></i></router-link>'
									+ '</el-menu-item>'
								+ '</el-menu>'
							+ '</div>'
						+ '</nav>'
					+ '</div>'
				+ '</el-header>'
				+ '<el-container>'
					+ '<el-aside v-if="showAside" class="admin-aside" :width="asideWidth">'
						+ '<div class="side-menu">'
							+ '<div class="admin-aside-top">'
								+ '<div class="userinfo"><img :src="loginUser.userIconUrl"/><label>{{asideUserTitle}}</label></div>'
							+ '</div>'
							+ '<admin-aside-menu :default-active="asideMenuActive" :menu-list="asideMenuList" :menu-class="asideMenuClass" :collapse="aisdeMenuCollapse" @select="onAsideMenuSelected"></admin-aside-menu>'
						+ '</div>'
					+ '</el-aside>'
					+ '<el-main class="admin-main"><slot></slot></el-main>'
				+ '</el-container>'
			+ '</el-container>',
	props: {
		appTitle: {
			type: String,
			default: '统一用户权限管理系统'
		},
		appLogoUrl: {
			type: String,
			default: APP_UPMS_WEB.CONTEXT_PATH + '/static/images/logoicon.png'
		},
		topnavMenuClass: {
			type: Array,
			default: ['admin-topnav-menu']
		},
		topnavMenuList: {
			type: Array,
			default: []
		},
		topnavMenuActive: {
			type: String,
			default: ''
		},
		showAside: {
			type: Boolean,
			default: true
		},
		asideMenuClass: {
			type: Array,
			default: ['admin-aside-menu']
		},
		asideMenuList: {
			type: Array,
			default: []
		},
		asideMenuActive: {
			type: String,
			default: ''
		},
		defaultAsideMenuCollapse: {
			type: Boolean,
			default: false
		},
		loginUser: {
			type: Object,
			default: {}
		}
	},
	data: function(){
		return {
			windowHeight: WINDOW_HEIGHT + 'px',
			headerHeight: TOP_NAV_MENU_HEIGHT + 'px',
			aisdeMenuCollapse: false,
			asideWidth: ASIDE_MENU_EXPANDED_WIDTH + 'px'
		};
	},
	computed: {
		asideUserTitle: function(){
			if(this.loginUser && this.loginUser.realName) {
				return '您好，' + this.loginUser.realName;
			} else {
				return '';
			}
		},
		logoutUrl: function(){
			return APP_UPMS_WEB.CONTEXT_PATH + "/logout";
		},
		userProfileUrl: function(){
			return APP_UPMS_WEB.CONTEXT_PATH + "/user/profile.html";
		},
		userSettingsUrl: function(){
			return APP_UPMS_WEB.CONTEXT_PATH + "/user/settings.html";
		}
	},
	created: function(){
		var sessionAsideMenuCollapse = sessionStorage.getItem(ASIDE_MENU_COLLAPSE_STATE_KEY);
		this.aisdeMenuCollapse = sessionAsideMenuCollapse ? (sessionAsideMenuCollapse === 'true' ? true : false) : this.defaultAsideMenuCollapse;
		if(this.aisdeMenuCollapse){
			this.asideWidth = ASIDE_MENU_COLLAPSED_WIDTH + 'px';
		} else {
			this.asideWidth = ASIDE_MENU_EXPANDED_WIDTH + 'px';
		}
	},
	methods: {
		toggleCollapseAsideMenu: function() {
			if(this.aisdeMenuCollapse){
				this.aisdeMenuCollapse = false;
				this.asideWidth = ASIDE_MENU_EXPANDED_WIDTH + 'px';
			} else {
				this.aisdeMenuCollapse = true;
				this.asideWidth = ASIDE_MENU_COLLAPSED_WIDTH + 'px';
			}
			sessionStorage.setItem(ASIDE_MENU_COLLAPSE_STATE_KEY, this.aisdeMenuCollapse);
		},
		onAsideMenuSelected: function(index, indexPath){
		}
	}
});
Vue.component('admin-container', AdminContainer); //注册全局组件

/**
 * 页面跳转
 */
var goToUrl = function(to, from, next){
	next(false); //终止原本导航
	//window.location.href = APP_UPMS_WEB.CONTEXT_PATH + to.meta.menuUrl;
	window.location.href = to.meta.menuUrl;
};
/**
 * 用户退出
 */
var logOut = function(to, from, next){
	next(false); //终止原本导航
	axios.get(APP_UPMS_API.CONTEXT_PATH + '/logout').then(function(response){
		if(response.data){
			var result = response.data;
			if(result.success){
				window.location.href = APP_UPMS_WEB.CONTEXT_PATH + '/login.html';
			}
		}
	});
};
/**
 * 全局路由
 */
var AdminRouter = new VueRouter({
	mode: 'history',
	routes: [{
		path: APP_UPMS_WEB.CONTEXT_PATH + '/user/profile.html', //用户资料
		meta: {
			menuUrl: APP_UPMS_WEB.CONTEXT_PATH + '/user/profile.html'
		},
		beforeEnter: goToUrl
	},{
		path: APP_UPMS_WEB.CONTEXT_PATH + '/user/settings.html', //用户设置
		meta: {
			menuUrl: APP_UPMS_WEB.CONTEXT_PATH + '/user/settings.html'
		},
		beforeEnter: goToUrl
	},{
		path: APP_UPMS_WEB.CONTEXT_PATH + '/logout', //用户退出
		meta: {
			menuUrl: APP_UPMS_WEB.CONTEXT_PATH + '/logout'
		},
		beforeEnter: logOut
	}]
});
/**
 * 插件(其他功能模块继承)
 * 注意其他功能模块的data,methods不能命名不能与以下有冲突!!!
 */
var AdminCommonsMixin = {
	router: AdminRouter,
	data: {
		pageVisible: false,
		pageScrollbarStyles: [{
			maxHeight: WINDOW_HEIGHT + 'px'
		}],
		asideMenuActive: '',
		asideMenuList: [],
		topnavMenuActive: '',
		topnavMenuList: [],
		loginUser: {},
		
		//{menuUrl: menuPath}
		userMenusMapping: {},
		//{appWebContextPath: menuPath}
		appIndexMenuMapping: {}
	},
	created: function(){
		this.loadLoginUserAllInfo(function(){
			if(FULL_SCREEN_LOADING){
				FULL_SCREEN_LOADING.close(); //关闭整页loading效果
			}
			this.pageVisible = true;
		}, 1500);
	},
	mounted: function(){
		//this.pageVisible = true;
	},
	methods: {
		applyDefaultActiveMenu: function(){
			var currentRoute = this.$router.currentRoute;
			//alert(currentRoute.path);
			if(currentRoute && currentRoute.path){
				var path = currentRoute.path;
				var start = path.indexOf('/', 1);
				if(start > 0){
					var appWebContextPath = path.substring(0, start);
					this.topnavMenuActive = this.appIndexMenuMapping[appWebContextPath];
					this.asideMenuActive = this.userMenusMapping[path];
				}
			}
		},
		/**
		 * 公共页面所有接口数据整合ALL-IN-ONE
		 */
		loadLoginUserAllInfo: function(finishCallback, callbackTimeoutMillis){
			callbackTimeoutMillis = callbackTimeoutMillis ? callbackTimeoutMillis : 0;
			var startTimeMillis = new Date().getTime();
			var _this = this;
			axios.get(APP_UPMS_API.CONTEXT_PATH + '/login/user/allinfo').then(function(response){
				if(response.data){
					var result = response.data;
					if(result && result.success){
						var allInfo = result.data;
						_this.loginUser = allInfo.loginUserInfo; //当前登录用户信息
						
						var asideMenuList = _this.prepareAsideMenuList(allInfo.loginUserAsideMenus); //当前登录用户看见的当前应用的菜单树
						var topnavMenuList = allInfo.loginUserTopnavMenus; //当前登录用户看见的应用的首页菜单
						var routes = [];
						_this.loadAsideMenuRouters(asideMenuList, routes);
						_this.loadTopnavMenuRouters(topnavMenuList, routes);
						
						_this.applyDefaultActiveMenu();
						
						_this.asideMenuList = asideMenuList;
						_this.topnavMenuList = topnavMenuList;
						_this.$router.addRoutes(routes);
					}else{
						_this.$message.error('获取公共页面所有接口数据出错!');
					}
					if(callbackTimeoutMillis <= 0){
						finishCallback.call(_this, result);
					}else{
						var endTimeMillis = new Date().getTime();
						var loadDataTimeMillis = endTimeMillis - startTimeMillis;
						var remainLoadingTimeMillis = callbackTimeoutMillis - loadDataTimeMillis;
						if(remainLoadingTimeMillis <= 0){
							finishCallback.call(_this, result);
						}else{
							setTimeout(function(){
								finishCallback.call(_this, result);
							}, remainLoadingTimeMillis);
						}
					}
				}
			}).catch(function(error){
				_this.$message.error('获取公共页面所有接口数据出错!');
			});
		},
		/**
		 * 预处理后台加载过来的用户菜单列表
		 */
		prepareAsideMenuList: function(userMenuList){
			var menuList = [];
			if(userMenuList && userMenuList.length){
				if(userMenuList.length == 1){
					var rootMenu = userMenuList[0];
					if(rootMenu.menuLevel == 0){
						menuList = rootMenu.subMenuList;
					}else{
						menuList = userMenuList;
					}
				}else{
					menuList = userMenuList;
				}
			}
			return menuList;
		},
		/**
		 * 递归动态加载左侧树型菜单的路由
		 */
		loadAsideMenuRouters: function(menuList, routes){
			if(menuList && menuList.length){
				var _this = this;
				menuList.forEach(function(node){
					var route = _this.createRoute(node);
					_this.userMenusMapping[route.meta.menuUrl] = route.path;
					routes.push(route);
					if(node.subMenuList){
						_this.loadAsideMenuRouters(node.subMenuList, routes);
					}
				});
			}
		},
		/**
		 * 加载顶部应用首页菜单的路由
		 */
		loadTopnavMenuRouters: function(menuList, routes){
			if(menuList && menuList.length){
				var _this = this;
				menuList.forEach(function(node){
					var route = _this.createRoute(node);
					_this.appIndexMenuMapping[node.appWebContextPath] = route.path;
					routes.push(route);
				});
			}
		},
		createRoute: function(node){
			var route = {
				path: node.menuPath,
				meta: {
					menuUrl: node.appWebContextPath + node.menuUrl
				},
				beforeEnter: goToUrl
			};
			return route;
		}
	}
};

/** 常用工具方法 */
function hasClass(el, cls) {
    return el.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}
function addClass(el, cls) {
    if (!hasClass(el, cls)) el.className += " " + cls;
}
function removeClass(el, cls) {
    if (hasClass(el, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        el.className = el.className.replace(reg, ' ');
    }
}
/**
 * 获取当前浏览器页面的url后面的请求参数
 * @param name
 * @returns
 */
function getQueryString(name) {
	return getQueryString(window.location.href, name); 
}
/**
 * 获取指定url后面的请求参数
 * @param url
 * @param name
 * @returns
 */
function getQueryString(url, name) {
	if(url && name){
		var index = url.indexOf('?');
		if(index > 0){
			var querys = url.substr(index + 1);
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
			var r = querys.match(reg); 
			if (r != null) return unescape(r[2]);
		}
	}
	return null; 
}
/**
 * 解决input readonly样式各浏览器之间的差异问题
 * @param event
 * @returns
 */
function onFocus4ReadOnly(event){
	event.target.blur();
}
/**
 * 转换<el-table/>的default-sort属性与后台字段对应
 * @param sort
 * @returns	 {'orders': 'myFieldName:asc'}
 */
function convertToSort(sort){
	var obj = {};
	if(sort.prop && sort.order){
		var direction = sort.order.toLowerCase() == 'descending' ? 'desc' : 'asc';
		obj["orders"] = sort.prop + ":" + direction;
	}
	return obj;
}
/**
 * JSON对象转成URL GET参数,例如：{name:'张三',age:23}  ==> name=张三&age=23
 * @param obj
 * @returns
 */
function objectToParam(obj){
	var params = [];
	if(obj){
		for(var name in obj){
			params.push(name + '=' + obj[name]);
		}
	}
	if(params.length){
		return params.join('&');
	}
	return '';
}