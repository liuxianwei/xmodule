var UPMS_USER_TYPE_SYSTEM = 0;
var IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png'];
var UPMS_USER_STATUS = {
	1: '启用',
	0: '禁用'
};
var vm = new Vue({
	el: '#app',
	mixins: [AdminCommonsMixin],
	data: {
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
		userList: [],
		currentActionType: 'add',
		userEditDialogVisible: false,
		userViewConfigDialogVisible: false,
		userViewConfigActiveTabName: 'userDetail',
		uploadHeaders: {
			Accept: 'application/json'
		},
		userIconUploadUrl: APP_UPMS_API.CONTEXT_PATH + '/xupload/image/submit',
		userIconUploadParam: {
			formatLimit: 'jpeg,jpg,png',
			pixelLimit: '90x90'
		},
		userEditForm: {
			userId: '',
			userName: '',
			password: '',
			repassword: '',
			realName: '',
			mobilePhone: '',
			email: '',
			userIcon: '',
			userIconUrl: ''
		},
		userEditRules: {
			userName: [{
				required: true,
				message: '请输入用户名!'
			},{
				validator: regex,
				regex: /^[a-zA-Z]{1}[a-zA-Z0-9_]{2,19}$/,
				message: '用户名必须由字母开头,3~20个字母、数字及下划线组成!'
			}],
			password: [{
				required: true,
				message: '请输入账户密码!'
			},{
				validator: regex,
				regex: /^[a-zA-Z0-9]{6,20}$/,
				message: '账户密码由6~20个字母或数字组成!'
			}],
			repassword: [{
				required: true,
				message: '请再次输入账户密码!'
			},{
				validator: regex,
				regex: /^[a-zA-Z0-9]{6,20}$/,
				message: '账户密码由6~20个字母或数字组成!'
			},{
				validator: equalTo,
				compareTarget: {
					vue: 'vm',
					form: 'userEditForm',
					field: 'password'
				},
				message: '两次密码不一致!'
			}],
			realName: [{
				required: true,
				message: '请输入姓名!'
			}],
			mobilePhone: [{
				required: true,
				message: '请输入手机号码!'
			},{
				validator: regex,
				regex: /^1[0-9]{2}[0-9]{8}$/,
				message: '手机号码不合法!'
			}],
			email: [{
				required: true,
				message: '请输入EMAIL!'
			},{
				validator: email,
				message: 'EMAIL不合法!'
			}]
		},
		userViewConfigForm: {
			userId: '',
			userName: '',
			realName: '',
			mobilePhone: '',
			email: '',
			userIcon: '',
			userIconUrl: '',
			status: '',
			statusName: '',
			userType: '',
			userTypeName: '',
			createBy: '',
			createTime: '',
			updateBy: '',
			updateTime: '',
			lastLoginTime: '',
			loginTimes: ''
		},
		userRoleList: [],
		userRoleQueryLoading: false,
		roleList: [],
		roleQueryLoading: false,
		selectedRoleIds: [],
		roleQuering: false,
		userRoleQueryForm: {
			appId: '',
			userId: '',
			roleName: '',
			roleCode: ''
		},
		roleQueryForm: {
			appId: '',
			roleName: '',
			roleCode: '',
			pageSize: 10,
			currentPage: 1,
			orderby: 'createTime',
			order: 'desc'
		},
		roleQueryTotal: 0,
		allAppList: [],
		changeUserPwdForm: {
			userId: '',
			userName: '',
			password: '',
			repassword: ''
		},
		changeUserPwdRules: {
			password: [{
				required: true,
				message: '请输入账户密码!'
			},{
				validator: regex,
				regex: /^[a-zA-Z0-9]{6,20}$/,
				message: '账户密码由6~20个字母或数字组成!'
			}],
			repassword: [{
				required: true,
				message: '请再次输入账户密码!'
			},{
				validator: regex,
				regex: /^[a-zA-Z0-9]{6,20}$/,
				message: '账户密码由6~20个字母或数字组成!'
			},{
				validator: equalTo,
				compareTarget: {
					vue: 'vm',
					form: 'changeUserPwdForm',
					field: 'password'
				},
				message: '两次密码不一致!'
			}]
		},
		changeUserPwdDialogVisible: false
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
		},
		userEditDialogTitle: function(){
			if(this.currentActionType == 'add'){
				return '新建用户';
			}else if(this.currentActionType == 'edit'){
				return '修改用户';
			}else{
				return '';
			}
		},
		userViewConfigDialogTitle: function(){
			if(this.currentActionType == 'view'){
				return '查看用户详情';
			}else if(this.currentActionType == 'config'){
				return '用户角色配置';
			}else{
				return '';
			}
		}
	},
	created: function(){
		this.loadAllAppList();
	},
	mounted: function(){
		this.queryUserList(1000);
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
		getRoleTypeTagType: function(roleType){
			if(roleType == 0){
				return "success";
			}else{
				return "primary";
			}
		},
		loadAllAppList: function(){
			var url = APP_UPMS_API.CONTEXT_PATH + '/app/all';
			var _this = this;
			axios.get(url, {
				params: {enabled: true}
			}).then(function(response){
				var result = response.data;
				if(result.success){
					_this.allAppList = result.data;
				}
			}).catch(function(error){
				_this.$message.error('获取所有应用列表出错!');
			});
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
				var params = Object.assign({}, _this.userQueryForm, convertToSort(_this.userQuerySort));
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
	    		this.openUserEditDialog(cmd, row);
	    	}else if(cmd == 'view'){
	    		this.openUserViewConfigDialog(cmd, row);
	    	}else if(cmd == 'delete'){
	    		this.deleteUser(row);
	    	}else if(cmd == 'config'){
	    		this.openUserViewConfigDialog(cmd, row);
	    	}else if(cmd == 'changepwd'){
	    		this.openChangeUserPwdDialog(cmd, row);
	    	}
	    },
	    deleteUser: function(row){
	    	if(UPMS_USER_TYPE_SYSTEM == row.userType){
				this.$message.error('系统用户不能删除!');
			}else{
				var url = APP_UPMS_API.CONTEXT_PATH + '/user/del/' + row.userId;
				var _this = this;
				this.$confirm('你确定要删除该用户?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.$message.success('删除成功!');
							_this.queryUserList(1000);
						}else{
							_this.$message.error(result.message);
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				});
			}
	    },
	    changeUserPassword: function(){
	    	var url = APP_UPMS_API.CONTEXT_PATH + '/user/changepwd/submit?forceUpdate=true';
			var _this = this;
			this.$refs.changeUserPwdForm.validate(function(valid){
				if(valid && !_this.submiting){
					_this.$confirm('你确定要修改该用户的密码?', '提示', {
						confirmButtonText: '确定',
				        cancelButtonText: '取消',
				        type: 'warning'
					}).then(function(){
						_this.submiting = true;
						setTimeout(function(){
							axios.post(url, _this.changeUserPwdForm).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.closeChangeUserPwdDialog();
									_this.$message.success('修改成功!');
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					});
				}else{
					return false;
				}
			});
	    },
	    changeUserStatus: function(event, row){
	    	row.status = row.status == 1 ? 0 : 1;
	    	if(UPMS_USER_TYPE_SYSTEM == row.userType){
				this.$message.error('系统用户禁止此操作!');
			}else{
				//var switchVm = event.currentTarget.__vue__;
		    	var targetStatus = row.status == 1 ? 0 : 1;
		    	var url = '';
		    	var _this = this;
		    	if(targetStatus == 1){
		    		url = APP_UPMS_API.CONTEXT_PATH + '/user/enable/' + row.userId;
		    	}else{
		    		url = APP_UPMS_API.CONTEXT_PATH + '/user/disable/' + row.userId;
		    	}
		    	this.$confirm('你确定要' + UPMS_USER_STATUS[targetStatus] + '该用户?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							row.status = targetStatus;
							_this.$message.success(UPMS_USER_STATUS[targetStatus] + '成功!');
						}else{
							_this.$message.error(result.message);
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				});
			}
	    	event.preventDefault();
	    	return false;
	    },
		openUserEditDialog: function(cmd, row){
			this.currentActionType = cmd;
			this.userEditDialogVisible = true;
			var _this = this;
	    	if(cmd == 'edit'){
	    		Vue.nextTick(function(){
	    			_this.userEditForm.userId = row.userId;
	    			_this.userEditForm.userName = row.userName;
	    			_this.userEditForm.realName = row.realName;
	    			_this.userEditForm.mobilePhone = row.mobilePhone;
	    			_this.userEditForm.email = row.email;
	    			_this.userEditForm.userIcon = row.userIcon;
	    			_this.userEditForm.userIconUrl = row.userIconUrl;
	    		});
	    	}
		},
		closeUserEditDialog: function(){
			this.userEditDialogVisible = false;
			this.$refs.userEditForm.resetFields();
			this.userEditForm.userId = ''; //手动reset
		},
		openUserViewConfigDialog: function(cmd, row){
	    	this.currentActionType = cmd;
	    	this.userViewConfigDialogVisible = true;
	    	var _this = this;
	    	Vue.nextTick(function(){
	    		_this.userViewConfigForm.userId = row.userId;
	    		_this.userViewConfigForm.userName = row.userName;
	    		_this.userViewConfigForm.realName = row.realName;
	    		_this.userViewConfigForm.mobilePhone = row.mobilePhone;
	    		_this.userViewConfigForm.email = row.email;
	    		_this.userViewConfigForm.userIcon = row.userIcon;
	    		_this.userViewConfigForm.userIconUrl = row.userIconUrl;
	    		_this.userViewConfigForm.userType = row.userType;
	    		_this.userViewConfigForm.userTypeName = row.userTypeName;
	    		_this.userViewConfigForm.status = row.status;
	    		_this.userViewConfigForm.statusName = row.statusName;
	    		_this.userViewConfigForm.lastLoginTime = row.lastLoginTime;
	    		_this.userViewConfigForm.loginTimes = row.loginTimes;
	    		_this.userViewConfigForm.createBy = row.createBy;
	    		_this.userViewConfigForm.createTime = row.createTime;
	    		_this.userViewConfigForm.updateBy = row.updateBy;
	    		_this.userViewConfigForm.updateTime = row.updateTime;
	    		if(cmd == 'config'){
	    			_this.userViewConfigActiveTabName = 'userRoleConfig';
	    			//_this.queryUserRoleList(1500);
	    		}else{
	    			_this.userViewConfigActiveTabName = 'userDetail';
	    			//_this.queryUserRoleList(0);
	    		}
			});
	    },
		closeUserViewConfigDialog: function(){
			this.userViewConfigDialogVisible = false;
			this.resetUserRoleQueryForm();
			this.userRoleList = [];
			if(this.currentActionType == 'config'){
				this.resetRoleQueryForm();
				this.roleList = [];
				this.selectedRoleIds = [];
			}
		},
		openChangeUserPwdDialog: function(cmd, row){
	    	this.currentActionType = cmd;
	    	this.changeUserPwdDialogVisible = true;
	    	var _this = this;
	    	Vue.nextTick(function(){
	    		_this.changeUserPwdForm.userId = row.userId;
	    		_this.changeUserPwdForm.userName = row.userName;
			});
	    },
	    closeChangeUserPwdDialog: function(){
			this.changeUserPwdDialogVisible = false;
			this.$refs.changeUserPwdForm.resetFields();
		},
		saveUser: function(){
	    	var url = '';
			if(this.currentActionType == 'add'){
				url = APP_UPMS_API.CONTEXT_PATH + '/user/add/submit';
			}else if(this.currentActionType == 'edit'){
				url = APP_UPMS_API.CONTEXT_PATH + '/user/edit/submit';
			}
			if(url){
				var _this = this;
				this.$refs.userEditForm.validate(function(valid){
					if(valid && !_this.submiting){
						_this.submiting = true;
						setTimeout(function(){
							axios.post(url, _this.userEditForm).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.closeUserEditDialog();
									_this.$message.success('保存成功!');
									_this.queryUserList(1000);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}else{
						return false;
					}
				});
			}
	    },
	    queryUserRoleList: function(loading){
	    	if(this.userRoleQueryForm.appId){
	    		this.userRoleQueryForm.userId = this.userViewConfigForm.userId;
				var _this = this;
				var url = APP_UPMS_API.CONTEXT_PATH + '/user/roles';
				if(loading){
					this.userRoleQueryLoading = true;
				}
				setTimeout(function(){
					axios.get(url, {
						params: _this.userRoleQueryForm
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.userRoleList = result.data;
						}else{
							_this.userRoleList = [];
						}
						if(loading){
							_this.userRoleQueryLoading = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.userRoleList = [];
						if(loading){
							_this.userRoleQueryLoading = false;
						}
					});
				}, loading);
	    	}else{
	    		this.$message.error('请先选择角色所属应用!');
	    	}
		},
		resetUserRoleQueryForm: function(){
			this.$refs.userRoleQueryForm.resetFields();
		},
		addUserRoleConfig: function(){
			if(this.selectedRoleIds.length){
				var userId = this.userViewConfigForm.userId;
	    		var url = APP_UPMS_API.CONTEXT_PATH + "/user/config/add";
	    		if(!this.submiting){
	    			this.submiting = true;
	    			var _this = this;
					setTimeout(function(){
						axios.post(url, {
							userId: userId,
							roleIds: _this.selectedRoleIds.join(',')
						}).then(function(response){
							_this.submiting = false;
							var result = response.data;
							if(result.success){
								_this.$message.success('添加成功!');
								_this.userRoleQueryForm.appId = _this.roleQueryForm.appId;
								_this.userViewConfigActiveTabName = 'userRoleConfig';
								Vue.nextTick(function(){
									_this.queryUserRoleList(1000);
									_this.$refs.roleTable.clearSelection();
								});
							}else{
								_this.$message.error(result.message);
							}
						}).catch(function(error){
							_this.submiting = false;
							_this.$message.error('请求出错!');
						});
					}, 1500);
				}
			}else{
				this.$message.error('请选择需要添加的角色!');
			}
		},
		delUserRoleConfig: function(){
			if(this.selectedRoleIds.length){
				var _this = this;
				this.$confirm('您确定要删除该用户的所选角色?', '提示',{
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					var userId = _this.userViewConfigForm.userId;
		    		var url = APP_UPMS_API.CONTEXT_PATH + "/user/config/del";
		    		if(!_this.submiting){
		    			_this.submiting = true;
						setTimeout(function(){
							axios.post(url, {
								userId: userId,
								roleIds: _this.selectedRoleIds.join(',')
							}).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$message.success('删除成功!');
									_this.queryUserRoleList(1000);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}
				});
			}else{
				this.$message.error('请选择需要移除的角色!');
			}
		},
		showUserRoleAddTab: function(){
			this.userViewConfigActiveTabName = 'userRoleAdd';
			this.roleQueryForm.appId = this.userRoleQueryForm.appId;
		},
		onRoleQueryCurrentPageChange: function(currentPage){
	    	this.roleQueryForm.currentPage = currentPage;
	    	this.doQueryRoleList(1000);
	    },
	    queryRoleList: function(loading){
			this.roleQueryForm.currentPage = 1;
			this.doQueryRoleList(loading);
		},
		doQueryRoleList: function(loading){
			if(this.roleQueryForm.appId){
				var _this = this;
				this.roleQuering = true;
				var url = APP_UPMS_API.CONTEXT_PATH + '/role/list';
				if(loading){
					this.roleQueryLoading = true;
				}
				setTimeout(function(){
					var params = _this.roleQueryForm;
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.roleList = result.data;
							_this.roleQueryTotal = result.totalRowCount;
						}else{
							_this.roleList = [];
							_this.roleQueryTotal = 0;
						}
						if(loading){
							_this.roleQueryLoading = false;
						}
						_this.roleQuering = false;
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.roleQueryLoading = false;
						_this.roleQuering = false;
					});
				}, loading);
			}else{
				this.$message.error('请先选择角色所属应用!');
			}
		},
	    resetRoleQueryForm: function(){
			this.$refs.roleQueryForm.resetFields();
		},
		onUserRoleSelectionChange: function(selectedRows){
			if(selectedRows && selectedRows.length){
				this.selectedRoleIds = [];
				selectedRows.forEach(function(row){
					this.selectedRoleIds.push(row.roleId);
				}, this);
			}else{
				this.selectedRoleIds = [];
			}
		},
		onRoleSelectionChange: function(selectedRows){
			if(selectedRows && selectedRows.length){
				this.selectedRoleIds = [];
				selectedRows.forEach(function(row){
					this.selectedRoleIds.push(row.roleId);
				}, this);
			}else{
				this.selectedRoleIds = [];
			}
		},
		beforeUserIconUpload: function(file){
			var fileType = file.type;
			if(IMAGE_TYPES.indexOf(fileType) == -1){
				this.$message.error('上传图片只能是jpg、png格式!');
				return false;
			}
			if(file.size > 0 && (file.size / 1024) > 50){
				this.$message.error('上传图片大小不能超过50KB!');
				return false;
			}
			return true;
		},
		handleUserIconUploadSuccess: function(response, file, fileList){
			file.url = response.data.url;
			file.path = response.data.path;
			this.userEditForm.userIcon = response.data.path;
			this.userEditForm.userIconUrl = response.data.url;
		},
		handleUserIconRemove: function(file, fileList){
			if(file && file.path){
				axios.get(APP_UPMS_API.CONTEXT_PATH + '/xupload/remove/submit.do?path=' + file.path);
			}
		},
		handleUploadError: function(error, file, fileList){
			try {
				var result = JSON.parse(error.message);
				this.$message.error(result.message);
			} catch (e) {
				this.$message.error(error.message);
			}
		}
	},
	watch: {
		userViewConfigActiveTabName: function(curVal, oldVal){
			this.selectedRoleIds = [];
		}
	}
});