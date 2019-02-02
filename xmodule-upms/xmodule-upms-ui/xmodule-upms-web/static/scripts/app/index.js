var UPMS_APP_TYPE_CORE = 0;
var UPMS_APP_STATUS = {
	true: '启用',
	false: '禁用'
};
var vm = new Vue({
	el: '#app',
	mixins: [AdminCommonsMixin],
	data: {
		submiting: false,
		appQueryForm: {
			appName: '',
			enabled: '',
			appType: ''
		},
		appQuerySort: {
			prop: 'createTime',
			order: 'descending'
		},
		appQueryLoading: false,
		appQuering: false,
		appQueryTotal: 0,
		appList: [],
		currentActionType: 'add',
		appEditDialogVisible: false,
		appViewDialogVisible: false,
		appEditForm: {
			appId: '',
			appName: '',
			appKey: '',
			appSecret: '',
			appWebContextPath: '',
			appApiContextPath: ''
		},
		appEditRules: {
			appName: [{
				required: true,
				message: '请输入应用名称!'
			}],
			appKey: [{
				validator: regex,
				regex: /^[a-zA-Z0-9]{9,32}$/,
				message: '应用KEY由9~32个字母或数字组成!'
			}],
			appSecret: [{
				validator: regex,
				regex: /^[a-zA-Z0-9]{9,32}$/,
				message: '应用密钥由9~32个字母或数字组成!'
			}],
			appWebContextPath: [{
				required: true,
				message: '请输入应用前端ContextPath!'
			},{
				validator: regex,
				regex: /^\/[a-zA-Z0-9\_\-]{1,99}$/,
				message: '应用前端ContextPath不合法!'
			}],
			appApiContextPath: [{
				required: true,
				message: '请输入应用后端ContextPath!'
			},{
				validator: regex,
				regex: /^\/[a-zA-Z0-9\_\-]{1,99}$/,
				message: '应用后端ContextPath不合法!'
			}]
		},
		appViewForm: {
			appName: '',
			appKey: '',
			appSecret: '',
			appWebContextPath: '',
			appApiContextPath: '',
			appTypeName: '',
			description: '',
			enabled: '',
			createTime: ''
		}
	},
	computed: {
		appEditDialogTitle: function(){
			if(this.currentActionType == 'add'){
				return '新建应用';
			}else if(this.currentActionType == 'edit'){
				return '修改应用';
			}else{
				return '';
			}
		}
	},
	created: function(){
	},
	mounted: function(){
		this.queryAppList(1000);
	},
	methods: {
		getAppTypeTagType: function(appType){
			if(appType == 0){
				return "success";
			}else{
				return "primary";
			}
		},
		getStatusTagType: function(status){
			if(status){
				return "success";
			}else{
				return "danger";
			}
		},
		queryAppList: function(loading){
			this.appQueryForm.currentPage = 1;
			this.doQueryAppList(loading);
		},
		doQueryAppList: function(loading){
			var _this = this;
			this.appQuering = true;
			var url = APP_UPMS_API.CONTEXT_PATH + '/app/list';
			if(loading){
				this.appQueryLoading = true;
			}
			setTimeout(function(){
				var params = Object.assign({}, _this.appQueryForm, convertToOrderBy(_this.appQuerySort));
				axios.get(url, {
					params: params
				}).then(function(response){
					var result = response.data;
					if(result.success){
						_this.appList = result.data;
						_this.appQueryTotal = result.totalRowCount;
					}else{
						_this.appList = [];
						_this.appQueryTotal = 0;
					}
					if(loading){
						_this.appQueryLoading = false;
					}
					_this.appQuering = false;
				}).catch(function(error){
					_this.appQueryLoading = false;
					_this.appQuering = false;
					_this.$message.error('请求出错!');
				});
			}, loading);
		},
		resetAppQueryForm: function(){
			this.$refs.appQueryForm.resetFields();
		},
		onAppQuerySortChange: function(parameter){
			if(this.appQuerySort.prop != parameter.prop || this.appQuerySort.order != parameter.order){
				this.appQuerySort.prop = parameter.prop;
				this.appQuerySort.order = parameter.order;
				this.queryAppList(1000);
			}
		},
		handleAppCommand: function(cmd, row){
	    	if(cmd == 'edit'){
	    		this.openAppEditDialog(cmd, row);
	    	}else if(cmd == 'view'){
	    		this.openAppViewDialog(cmd, row);
	    	}else if(cmd == 'delete'){
	    		this.deleteApp(row);
	    	}
	    },
	    openAppEditDialog: function(cmd, row){
			this.currentActionType = cmd;
			this.appEditDialogVisible = true;
			var _this = this;
	    	if(cmd == 'edit'){
	    		Vue.nextTick(function(){
	    			_this.appEditForm.appId = row.appId;
	    			_this.appEditForm.appName = row.appName;
	    			_this.appEditForm.appKey = row.appKey;
	    			_this.appEditForm.appSecret = row.appSecret;
	    			_this.appEditForm.appWebContextPath = row.appWebContextPath;
	    			_this.appEditForm.appApiContextPath = row.appApiContextPath;
	    			_this.appEditForm.description = row.description;
	    		});
	    	}
		},
		closeAppEditDialog: function(){
			this.appEditDialogVisible = false;
			this.$refs.appEditForm.resetFields();
			this.appEditForm.appId = ''; //手动reset
		},
		openAppViewDialog: function(cmd, row){
	    	this.currentActionType = cmd;
	    	this.appViewDialogVisible = true;
	    	var _this = this;
	    	Vue.nextTick(function(){
	    		_this.appViewForm.appId = row.appId;
	    		_this.appViewForm.appName = row.appName;
	    		_this.appViewForm.appKey = row.appKey;
	    		_this.appViewForm.appSecret = row.appSecret;
	    		_this.appViewForm.appWebContextPath = row.appWebContextPath;
    			_this.appViewForm.appApiContextPath = row.appApiContextPath;
	    		_this.appViewForm.appTypeName = row.appTypeName;
	    		_this.appViewForm.description = row.description;
	    		_this.appViewForm.enabled = row.enabled;
	    		_this.appViewForm.createTime = row.createTime;
			});
	    },
		closeAppViewDialog: function(){
			this.appViewDialogVisible = false;
			this.$refs.appViewForm.resetFields();
		},
		deleteApp: function(row){
	    	if(UPMS_APP_TYPE_CORE == row.appType){
				this.$message.error('核心应用不能删除!');
			}else{
				var url = APP_UPMS_API.CONTEXT_PATH + '/app/del/' + row.appId;
				var _this = this;
				this.$confirm('你确定要删除该应用?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.$message.success('删除成功!');
							_this.queryAppList(1000);
						}else{
							_this.$message.error(result.message);
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				});
			}
	    },
		changeAppStatus: function(event, row){
			row.enabled = !row.enabled;
	    	if(UPMS_APP_TYPE_CORE == row.appType){
				this.$message.error('核心应用禁止此操作!');
			}else{
				//var switchVm = event.currentTarget.__vue__;
		    	var targetStatus = row.enabled ? false : true;
		    	var url = '';
		    	var _this = this;
		    	if(targetStatus == 1){
		    		url = APP_UPMS_API.CONTEXT_PATH + '/app/enable/' + row.appId;
		    	}else{
		    		url = APP_UPMS_API.CONTEXT_PATH + '/app/disable/' + row.appId;
		    	}
		    	this.$confirm('你确定要' + UPMS_APP_STATUS[targetStatus] + '该用户?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							row.enabled = targetStatus;
							_this.$message.success(UPMS_APP_STATUS[targetStatus] + '成功!');
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
	    saveApp: function(){
	    	var url = '';
			if(this.currentActionType == 'add'){
				url = APP_UPMS_API.CONTEXT_PATH + '/app/add/submit';
			}else if(this.currentActionType == 'edit'){
				url = APP_UPMS_API.CONTEXT_PATH + '/app/edit/submit';
			}
			if(url){
				var _this = this;
				this.$refs.appEditForm.validate(function(valid){
					if(valid && !_this.submiting){
						_this.submiting = true;
						setTimeout(function(){
							axios.post(url, _this.appEditForm).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.closeAppEditDialog();
									_this.$message.success('保存成功!');
									_this.queryAppList(1000);
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
	    randomGenerateByUUID: function(type){
	    	var url = APP_UPMS_API.CONTEXT_PATH + '/app/uuid/random';
	    	var _this = this;
	    	axios.get(url).then(function(response){
				var result = response.data;
				if(result.success){
					_this.appEditForm[type] = result.data;
				}else{
					_this.$message.error(result.message);
				}
			}).catch(function(error){
				_this.$message.error('请求出错!');
			});
	    }
	}
});