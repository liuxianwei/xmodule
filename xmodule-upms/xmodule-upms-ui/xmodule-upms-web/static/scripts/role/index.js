var UPMS_ROLE_TYPE_SYSTEM = 0;
var vm = new Vue({
	el: '#app',
	mixins: [AdminCommonsMixin],
	data: {
		submiting: false,
		allAppList: [],
		roleQueryForm: {
			appId: '',
			roleName: '',
			roleCode: '',
			roleType: '',
			pageSize: 10,
			currentPage: 1
		},
		roleQuerySort: {
			prop: 'createTime',
			order: 'descending'
		},
		roleQueryLoading: false,
		roleQuering: false,
		roleQueryTotal: 0,
		roleList: [],
		currentActionType: 'add',
		roleEditDialogVisible: false,
		roleViewConfigDialogVisible: false,
		roleViewConfigActiveTabName: 'roleDetail',
		roleEditForm: {
			roleId: '',
			roleName: '',
			roleCode: '',
			description: '',
			appId: ''
		},
		roleEditRules: {
			roleName: [{
				required: true,
				message: '请输入角色名称!'
			}],
			roleCode: [{
				required: true,
				message: '请输入角色代码!'
			},{
				validator: regex,
				regex: /^[a-zA-Z0-9_]{1,100}$/,
				message: '角色代码由不超过100个字母、数字、下划线组成!'
			}],
			appId: [{
				required: true,
				message: '请选择角色所属应用!'
			}]
		},
		roleViewConfigForm: {
			roleId: '',
			roleName: '',
			roleCode: '',
			description: '',
			roleType: '',
			roleTypeName: '',
			appId: '',
			appName: '',
			createBy: '',
			createTime: '',
			updateBy: '',
			updateTime: ''
		},
		roleResourceList: [],
		roleResourcesLoading: false,
		checkedResourceIdList: [],
		defaultProps: {
			children: 'children',
	        label: 'label'
		}
	},
	computed: {
		showRoleQueryPager: function(){
			if(this.roleQuering || this.roleQueryTotal == 0){
				return false;
			}else{
				return true;
			}
		},
		roleEditDialogTitle: function(){
			if(this.currentActionType == 'add'){
				return '新建角色';
			}else if(this.currentActionType == 'edit'){
				return '修改角色';
			}else{
				return '';
			}
		},
		roleViewConfigDialogTitle: function(){
			if(this.currentActionType == 'view'){
				return '查看角色详情';
			}else if(this.currentActionType == 'config'){
				return '角色角色配置';
			}else{
				return '';
			}
		}
	},
	created: function(){
		this.loadAllAppList();
	},
	mounted: function(){
		//this.queryRoleList(1000);
	},
	methods: {
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
		queryRoleList: function(loading){
			if(this.roleQueryForm.appId){
				this.roleQueryForm.currentPage = 1;
				this.doQueryRoleList(loading);
			}else{
				this.$message.error('请先选择角色所属应用!');
			}
		},
		doQueryRoleList: function(loading){
			var _this = this;
			this.roleQuering = true;
			var url = APP_UPMS_API.CONTEXT_PATH + '/role/list';
			if(loading){
				this.roleQueryLoading = true;
			}
			setTimeout(function(){
				var params = Object.assign({}, _this.roleQueryForm, convertToSort(_this.roleQuerySort));
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
					_this.roleQueryLoading = false;
					_this.roleQuering = false;
					_this.$message.error('请求出错!');
				});
			}, loading);
		},
		resetRoleQueryForm: function(){
			this.$refs.roleQueryForm.resetFields();
		},
		onRoleQuerySortChange: function(parameter){
			if(this.roleQuerySort.prop != parameter.prop || this.roleQuerySort.order != parameter.order){
				this.roleQuerySort.prop = parameter.prop;
				this.roleQuerySort.order = parameter.order;
				this.queryRoleList(1000);
			}
		},
		onRoleQueryPageSizeChange: function(event, pageSize){
	    	this.roleQueryForm.pageSize = pageSize;
	    	this.queryRoleList(1000);
	    },
	    onRoleQueryCurrentPageChange: function(currentPage){
	    	this.roleQueryForm.currentPage = currentPage;
	    	this.doQueryRoleList(1000);
	    },
	    handleRoleCommand: function(cmd, row){
	    	if(cmd == 'edit'){
	    		this.openRoleEditDialog(cmd, row);
	    	}else if(cmd == 'view'){
	    		this.openRoleViewConfigDialog(cmd, row);
	    	}else if(cmd == 'delete'){
	    		this.deleteRole(row);
	    	}else if(cmd == 'config'){
	    		this.openRoleViewConfigDialog(cmd, row);
	    	}
	    },
	    deleteRole: function(row){
	    	if(UPMS_ROLE_TYPE_SYSTEM == row.roleType){
				this.$message.error('系统角色不能删除!');
			}else{
				var url = APP_UPMS_API.CONTEXT_PATH + '/role/del/' + row.roleId;
				var _this = this;
				this.$confirm('你确定要删除该角色?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning'
				}).then(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.$message.success('删除成功!');
							_this.queryRoleList(1000);
						}else{
							_this.$message.error(result.message);
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				});
			}
	    },
		openRoleEditDialog: function(cmd, row){
			if(this.roleQueryForm.appId){
				this.currentActionType = cmd;
				this.roleEditDialogVisible = true;
				var _this = this;
		    	if(cmd == 'edit'){
		    		Vue.nextTick(function(){
		    			_this.roleEditForm.roleId = row.roleId;
		    			_this.roleEditForm.roleName = row.roleName;
		    			_this.roleEditForm.roleCode = row.roleCode;
		    			_this.roleEditForm.appId = row.appId;
		    			_this.roleEditForm.description = row.description;
		    		});
		    	}
			}else{
				this.$message.error('请先选择角色所属应用!');
			}
		},
		closeRoleEditDialog: function(){
			this.roleEditDialogVisible = false;
			this.$refs.roleEditForm.resetFields();
			this.roleEditForm.roleId = ''; //手动reset
		},
		openRoleViewConfigDialog: function(cmd, row){
	    	this.currentActionType = cmd;
	    	this.roleViewConfigDialogVisible = true;
	    	var _this = this;
	    	Vue.nextTick(function(){
	    		_this.roleViewConfigForm.roleId = row.roleId;
	    		_this.roleViewConfigForm.roleName = row.roleName;
	    		_this.roleViewConfigForm.roleCode = row.roleCode;
	    		_this.roleViewConfigForm.roleType = row.roleType;
	    		_this.roleViewConfigForm.roleTypeName = row.roleTypeName;
	    		_this.roleViewConfigForm.description = row.description;
	    		_this.roleViewConfigForm.appId = row.appId;
	    		_this.roleViewConfigForm.appName = row.appName;
	    		_this.roleViewConfigForm.createBy = row.createBy;
	    		_this.roleViewConfigForm.createTime = row.createTime;
	    		_this.roleViewConfigForm.updateBy = row.updateBy;
	    		_this.roleViewConfigForm.updateTime = row.updateTime;
	    		if(cmd == 'config'){
	    			_this.roleViewConfigActiveTabName = 'roleResourceConfig';
	    			_this.loadRoleResourceList(1500, _this.roleViewConfigForm.appId, row.roleId);
	    		}else{
	    			_this.roleViewConfigActiveTabName = 'roleDetail';
	    			_this.loadRoleResourceList(0, _this.roleViewConfigForm.appId, row.roleId);
	    		}
			});
	    },
		closeRoleViewConfigDialog: function(){
			this.roleViewConfigDialogVisible = false;
			this.roleResourceList = [];
			this.checkedResourceIdList = [];
		},
		saveRole: function(){
			if(this.roleQueryForm.appId){
				this.roleEditForm.appId = this.roleQueryForm.appId;
				var url = '';
				if(this.currentActionType == 'add'){
					url = APP_UPMS_API.CONTEXT_PATH + '/role/add/submit';
				}else if(this.currentActionType == 'edit'){
					url = APP_UPMS_API.CONTEXT_PATH + '/role/edit/submit';
				}
				if(url){
					var _this = this;
					this.$refs.roleEditForm.validate(function(valid){
						if(valid && !_this.submiting){
							_this.submiting = true;
							setTimeout(function(){
								axios.post(url, _this.roleEditForm).then(function(response){
									_this.submiting = false;
									var result = response.data;
									if(result.success){
										_this.closeRoleEditDialog();
										_this.$message.success('保存成功!');
										_this.queryRoleList(1000);
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
			}else{
				this.$message.error('请先选择角色所属应用!');
			}
	    },
		saveRoleResourceConfig: function(){
	    	var roleId = this.roleViewConfigForm.roleId;
	    	var appId = this.roleViewConfigForm.appId;
	    	var checkedKeys = this.$refs.roleResourceViewTree.getCheckedKeys();
	    	var checkedResourceIds = checkedKeys.sort().join(",");
	    	if(checkedResourceIds == this.checkedResourceIdList.sort().join(",")){
	    		this.$message.warning('配置没有改变,无需保存!');
	    	}else{
	    		var url = APP_UPMS_API.CONTEXT_PATH + "/role/config/submit";
	    		if(!this.submiting){
	    			this.submiting = true;
	    			var _this = this;
					setTimeout(function(){
						axios.post(url, {
							roleId: roleId,
							resourceIds: checkedResourceIds
						}).then(function(response){
							_this.submiting = false;
							var result = response.data;
							if(result.success){
								_this.$message.success('保存成功!');
								_this.loadRoleResourceList(1500, appId, roleId);
							}else{
								_this.$message.error(result.message);
							}
						}).catch(function(error){
							_this.submiting = false;
							_this.$message.error('请求出错!');
						});
					}, 1500);
				}
	    	}
	    },
		refreshRoleResourceConfig: function(){
			var roleId = this.roleViewConfigForm.roleId;
	    	var appId = this.roleViewConfigForm.appId;
			this.loadRoleResourceList(1500, appId, roleId);
		},
		loadRoleResourceList: function(loading, appId, roleId){
			var _this = this;
			var url = APP_UPMS_API.CONTEXT_PATH + '/role/resources/' + appId + '/' + roleId;
			if(loading){
				this.roleResourcesLoading = true;
			}
			setTimeout(function(){
				axios.get(url).then(function(response){
					var result = response.data;
					if(result.success){
						_this.roleResourceList = result.data.allResourceList;
						_this.checkedResourceIdList = result.data.checkedResourceIdList;
						Vue.nextTick(function(){
							_this.$refs.roleResourceViewTree.setCheckedKeys(result.data.checkedResourceIdList);
						});
					}else{
						_this.roleResourceList = [];
						_this.checkedResourceIdList = [];
					}
					if(loading){
						_this.roleResourcesLoading = false;
					}
				}).catch(function(error){
					_this.$message.error('请求出错!');
					_this.roleResourceList = [];
					_this.checkedResourceIdList = [];
					if(loading){
						_this.roleResourcesLoading = false;
					}
				});
			}, loading);
		},
		renderRoleResourceTreeNode: function(h, param){
			var _this = this;
			return h('span', {
				'class': 'el-tree-custom-resource el-clearfix'
			},[
				h('span', {
					'class': 'custom-label'
				}, param.node.label),
				h('div', {
					'class': 'custom-action-type'
				}, [
					h('el-tag', {
						props: {
							size: 'small',
							type: _this.getTagType4ActionType(param.data.actionType)
						}
					}, param.data.actionTypeName)
				])
			]);
		},
		onRoleResourceTreeNodeCheckClick: function(data, state){
			if(this.currentActionType == 'config'){
				var node = this.$refs.roleResourceViewTree.getNode(data);
				if(node.checked){
					this.recursiveCheckParent(node, true); //当前节点被选中时则选中其所有父节点
				}else{
					this.recursiveCheckChild(node, false); //当前节点被取消选中时则取消选中其所有子节点
				}
			}
		},
		recursiveCheckParent: function(node, checked){
			var parent = node.parent;
			if(parent){
				parent.checked = checked;
				this.recursiveCheckParent(parent, checked);
			}
		},
		recursiveCheckChild: function(node, checked){
			var childs = node.childNodes;
			if(childs.length){
				var _this = this;
				childs.forEach(function(child){
					child.checked = checked;
					_this.recursiveCheckChild(child, checked);
				});
			}
		},
		getTagType4ActionType: function(actionType){
			return actionType == 0 ? 'primary' : 'success'
		}
	},
	watch: {
	}
});