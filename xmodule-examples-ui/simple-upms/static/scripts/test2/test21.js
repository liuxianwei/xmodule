var vm = new Vue({
	el: '#app',
	data: {
		pageVisible: false,
		diablePageLoading: true,
		loginUser: null
	},
	created: function(){
		var _this = this;
		loadCurrentLoginUser(vm, function(user){
			_this.loginUser = user;
		}, function(){
			_this.$message.error('获取当前登录用户信息失败!');
		});
	},
	mounted: function(){
		this.pageVisible = true;
	},
	methods: {
	}
});