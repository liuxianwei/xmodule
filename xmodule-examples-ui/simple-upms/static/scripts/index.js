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
		logout: function(){
			var _this = this;
			var url = APP_UPMS_API.CONTEXT_PATH + '/logout';
			axios.get(url).then(function(response){
				var result = response.data;
				if(result.success){
					window.location.href = result.data;
				}else{
					_this.$message.error(result.message);
				}
			}).catch(function(error){
				_this.$message.error('请求出错!');
			});
		}
	}
});