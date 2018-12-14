var vm = new Vue({
	el: '#app',
	data: {
		pageVisible: false,
		diablePageLoading: true,
		submiting: false,
		userQueryForm: {
			userName: '',
			realName: '',
			userType: '',
			status: '',
			pageSize: 10,
			currentPage: 1
		},
		userQuerySort: {
			prop: 'createTime',
			order: 'descending'
		},
		userQueryLoading: false,
		userQuering: false,
		userQueryTotal: 0,
		userList: []
	},
	computed: {
		showUserQueryPager: function(){
			if(this.userQuering || this.userQueryTotal == 0){
				return false;
			}else{
				return true;
			}
		},
		showRoleQueryPager: function(){
			if(this.roleQuering || this.roleQueryTotal == 0){
				return false;
			}else{
				return true;
			}
		}
	},
	created: function(){
	},
	mounted: function(){
		this.pageVisible = true;
	},
	methods: {
		getUserTypeTagType: function(userType){
			if(userType == 0){
				return "success";
			}else{
				return "primary";
			}
		},
		getStatusTagType: function(status){
			if(status == 0){
				return "danger";
			}else{
				return "success";
			}
		},
		queryUserList: function(loading){
			this.userQueryForm.currentPage = 1;
			this.doQueryUserList(loading);
		},
		doQueryUserList: function(loading){
			var _this = this;
			this.userQuering = true;
			var url = APP_UPMS_API.CONTEXT_PATH + '/user/list';
			if(loading){
				this.userQueryLoading = true;
			}
			setTimeout(function(){
				var params = Object.assign({}, _this.userQueryForm, convertToOrderBy(_this.userQuerySort));
				axios.get(url, {
					params: params
				}).then(function(response){
					var result = response.data;
					if(result.success){
						_this.userList = result.data;
						_this.userQueryTotal = result.totalRowCount;
					}else{
						_this.userList = [];
						_this.userQueryTotal = 0;
					}
					if(loading){
						_this.userQueryLoading = false;
					}
					_this.userQuering = false;
				}).catch(function(error){
					_this.userQueryLoading = false;
					_this.userQuering = false;
					_this.$message.error('请求出错!');
				});
			}, loading);
		},
		resetUserQueryForm: function(){
			this.$refs.userQueryForm.resetFields();
		},
		onUserQuerySortChange: function(parameter){
			if(this.userQuerySort.prop != parameter.prop || this.userQuerySort.order != parameter.order){
				this.userQuerySort.prop = parameter.prop;
				this.userQuerySort.order = parameter.order;
				this.queryUserList(1000);
			}
		},
		onUserQueryPageSizeChange: function(event, pageSize){
	    	this.userQueryForm.pageSize = pageSize;
	    	this.queryUserList(1000);
	    },
	    onUserQueryCurrentPageChange: function(currentPage){
	    	this.userQueryForm.currentPage = currentPage;
	    	this.doQueryUserList(1000);
	    },
	    handleUserCommand: function(cmd, row){
	    	if(cmd == 'edit'){
	    		//this.openUserEditDialog(cmd, row);
	    	}else if(cmd == 'view'){
	    		//this.openUserViewConfigDialog(cmd, row);
	    	}else if(cmd == 'delete'){
	    		//this.deleteUser(row);
	    	}else if(cmd == 'config'){
	    		//this.openUserViewConfigDialog(cmd, row);
	    	}else if(cmd == 'changepwd'){
	    		//this.openChangeUserPwdDialog(cmd, row);
	    	}
	    }
	}
});