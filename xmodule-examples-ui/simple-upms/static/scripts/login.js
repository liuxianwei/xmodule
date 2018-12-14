
new Vue({
	el: '#app',
	data: {
		pageVisible: false,
		diablePageLoading: true,
		errorMessage: '',
		submiting: false,
		loginForm: {
			userName: '',
			password: ''
		},
		loginRules: {
			userName: [{
				required: true,
				message: '请输入用户名!'
			}],
			password: [{
				required: true,
				message: '请输入密码!'
			}]
		}
	},
	mounted: function(){
		this.pageVisible = true;
	},
	methods: {
		submitLogin: function(){
			var _this = this;
			this.$refs.loginForm.validate(function(valid){
				if(valid && !_this.submiting){
					_this.submiting = true;
					setTimeout(function(){
						axios.post(APP_UPMS_API.CONTEXT_PATH + '/login', _this.loginForm).then(function(response){
							var result = response.data;
							if(result.success){
								window.location.href = result.data;
							}else{
								_this.submiting = false;
								_this.errorMessage = result.message;
							}
						}).catch(function(error){
							_this.submiting = false;
							_this.$message.error('请求出错!');
						});
					}, 1000);
				}else{
					_this.errorMessage = '';
					return false;
				}
			});
		}
	}
});