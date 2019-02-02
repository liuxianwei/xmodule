var UPMS_RESOURCE_TYPE_SYSTEM = 0;
var UPMS_RESOURCE_ACTION_TYPE_MENU = 0;
var treeTopOffset = 215; //tree组件上边框与浏览器顶部的距离
var treeMaxHeight = WINDOW_HEIGHT - treeTopOffset;
var vm = new Vue({
	el: '#app',
	mixins: [AdminCommonsMixin],
	data: {
		submiting: false,
		allAppList: [],
		resourceQueryForm: {
			appId: '',
			onlyMenuActionType: false
		},
		resourceTreeLoading: false,
		resourceTreeList: [],
		defaultProps: {
			children: 'children',
	        label: 'label'
		},
		treeWrapStyle: [{
			maxHeight: treeMaxHeight + 'px'
		}],
		currentActionType: 'add',
		resourceEditDialogVisible: false,
		resourceViewDialogVisible: false,
		resourceEditForm: {
			resourceId: '',
			parentResourceId: '',
			parentResourceName: '',
			resourceName: '',
			permissionExpression: '',
			actionType: '',
			actionTypeEditable: true,
			resourceUrl: '',
			siblingsIndex: '',
			resourceIcon: 'fa-circle-o',
			appId: '',
			indexPage: false
		},
		resourceViewForm: {
			parentResourceName: '',
			resourceName: '',
			permissionExpression: '',
			actionType: '',
			actionTypeName: '',
			resourceUrl: '',
			siblingsIndex: '',
			resourceIcon: '',
			resourceTypeName: '',
			appName: ''
		},
		resourceEditRules: {
			parentResourceId: [{
				required: true,
				message: '未选择父级资源!'
			}],
			resourceName: [{
				required: true,
				message: '请输入资源名称!'
			}],
			permissionExpression: [{
				validator: regex,
				regex: /^\w+(\:[A-Za-z0-9_*]+(\,[A-Za-z0-9_*]+)*){1,2}$/,
				message: '请输入合法的权限表达式!'
			}],
			actionType: [{
				required: true,
				message: '请选择资源功能类型!'
			}],
			siblingsIndex: [{
				required: true,
				message: '请输资源排序号!'
			},{
				validator: regex,
				regex: /^\d+$/,
				message: '排序号必须是整数!'
			}]
		}
	},
	computed: {
		resourceEditDialogTitle: function(){
			if(this.currentActionType == 'add'){
				return '新建资源';
			}else if(this.currentActionType == 'edit'){
				return '修改资源';
			}else{
				return '';
			}
		}
	},
	created: function(){
		this.loadAllAppList();
	},
	mounted: function(){
		//this.refreshResourceTree(1000);
	},
	methods: {
		getResourceTypeTagType: function(resourceType){
			if(resourceType == 0){
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
		renderResourceTreeNode: function(h, node){
			var _this = this;
			return h('span', {
				'class': 'el-tree-custom-resource el-clearfix'
			},[
				h('span', {
					'class': 'custom-label'
				}, node.data.label),
				h('div', {
					'class': 'custom-options'
				},[
					h('el-dropdown', {
						'class': 'el-dropdown-small',
						props: {
							type: 'primary',
							splitButton: true,
							trigger: 'click',
							menuAlign: 'start'
						},
						on: {
							click: function(){
								_this.viewResource(node);
							},
							command: function(cmd){
								if(cmd == 'add'){
									_this.addResource(node);
								}else if(cmd == 'edit'){
									_this.editResource(node);
								}else if(cmd == 'del'){
									_this.delResource(node);
								}
							}
						}
					},[ 
						'查看详情',
						h('el-dropdown-menu', {
							'class': 'el-dropdown-menu-small',
							slot: 'dropdown'
						}, [
							h('el-dropdown-item', {
								props: {
									command: 'add'
								}
							}, '新增资源'),
							h('el-dropdown-item', {
								props: {
									command: 'edit'
								}
							}, '修改资源'),
							h('el-dropdown-item', {
								props: {
									divided: true,
									command: 'del'
								}
							}, '删除资源')
						])
					])
				]),
				h('div', {
					'class': 'custom-permission-exp'
				}, node.data.permissionExpression),
				h('div', {
					'class': 'custom-resource-url',
					domProps: {
						innerHTML: _this.formatResourceUrl(node.data.resourceUrl)
					}
				}),
				h('div', {
					'class': 'custom-action-type'
				}, [
					h('el-tag', {
						props: {
							size: 'small',
							type: _this.getTagType4ActionType(node.data.actionType)
						}
					}, node.data.actionTypeName)
				]),
				h('div', {
					'class': 'custom-siblings-index'
				}, node.data.siblingsIndex)
			]);
		},
		getTagType4ActionType: function(actionType){
			return actionType == 0 ? 'primary' : 'success'
		},
		formatResourceUrl: function(resourceUrl){
			if(resourceUrl){
				return resourceUrl.replace(/\r{0,}\n/g,"<br/>");
			}
			return '';
		},
		refreshResourceTree: function(loading){
			var appId = this.resourceQueryForm.appId;
			if(appId){
				var _this = this;
				var url = APP_UPMS_API.CONTEXT_PATH + '/resource/list/' + appId;
				if(loading){
					this.resourceTreeLoading = true;
				}
				setTimeout(function(){
					axios.get(url, {params: _this.resourceQueryForm}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.resourceTreeList = result.data;
						}else{
							_this.resourceTreeList = [];
						}
						if(loading){
							_this.resourceTreeLoading = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.resourceTreeLoading = false;
					});
				}, loading);
			}else{
				this.$message.error('请先选择资源所属应用!');
			}
		},
		resetResourceQueryForm: function(){
			this.$refs.resourceQueryForm.resetFields();
		},
		addResource: function(node){
			var appId = this.resourceQueryForm.appId;
			if(appId){
				this.currentActionType = 'add';
				
				if(this.currentActionType == 'add' && node.data.actionType != UPMS_RESOURCE_ACTION_TYPE_MENU){
					this.$message.error("只能在节点类型为'菜单'的资源下面新建子节点!");
					return;
				}
				
				this.resourceEditDialogVisible = true;
				var _this = this;
				Vue.nextTick(function(){ //nextTick必须放在resourceEditDialogVisible=true即对话框显示后执行,解决form.field初始值问题,进而解决form.resetFields()问题
					_this.resourceEditForm.resourceId = '';
					_this.resourceEditForm.appId = appId;
					_this.resourceEditForm.parentResourceId = node.data.id;
					_this.resourceEditForm.parentResourceName = node.data.label;
				});
			}else{
				this.$message.error('请先选择资源所属应用!');
			}
		},
		editResource: function(node){
			this.currentActionType = 'edit';
			this.resourceEditDialogVisible = true;
			//如果当前被修改节点存在子节点，那么它的actionType只能为'菜单'类型,不能修改
			this.resourceEditForm.actionTypeEditable = node.data.children && node.data.children.length ? false : true;
			var _this = this;
			Vue.nextTick(function(){ //nextTick必须放在resourceEditDialogVisible=true即对话框显示后执行,解决form.field初始值问题,进而解决form.resetFields()问题
				_this.resourceEditForm.resourceId = node.data.id;
				_this.resourceEditForm.resourceName = node.data.label;
				_this.resourceEditForm.parentResourceId = node.data.parentResourceId;
				_this.resourceEditForm.parentResourceName = node.data.parentResourceName;
				_this.resourceEditForm.permissionExpression = node.data.permissionExpression;
				_this.resourceEditForm.actionType = node.data.actionType;
				_this.resourceEditForm.resourceUrl = node.data.resourceUrl;
				_this.resourceEditForm.siblingsIndex = node.data.siblingsIndex;
				_this.resourceEditForm.resourceIcon = node.data.resourceIcon;
				_this.resourceEditForm.indexPage = node.data.indexPage;
				_this.resourceEditForm.appId = node.data.appId;
			});
		},
		viewResource: function(node){
			this.resourceViewDialogVisible = true;
			var _this = this;
			Vue.nextTick(function(){
				_this.resourceViewForm.parentResourceName = node.data.parentResourceName;
				_this.resourceViewForm.resourceName = node.data.label;
				_this.resourceViewForm.permissionExpression = node.data.permissionExpression;
				_this.resourceViewForm.actionType = node.data.actionType;
				_this.resourceViewForm.actionTypeName = node.data.actionTypeName;
				_this.resourceViewForm.resourceUrl = node.data.resourceUrl;
				_this.resourceViewForm.siblingsIndex = node.data.siblingsIndex;
				_this.resourceViewForm.resourceIcon = node.data.resourceIcon;
				_this.resourceViewForm.resourceTypeName = node.data.resourceTypeName;
				_this.resourceViewForm.appName = node.data.appName;
			});
		},
		saveResource: function(){
			var url = '';
			if(this.currentActionType == 'add'){
				url = APP_UPMS_API.CONTEXT_PATH + '/resource/add/submit';
			}else if(this.currentActionType == 'edit'){
				url = APP_UPMS_API.CONTEXT_PATH + '/resource/edit/submit';
			}
			if(url){
				var _this = this;
				this.$refs.resourceEditForm.validate(function(valid){
					if(valid && !_this.submiting){
						_this.submiting = true;
						setTimeout(function(){
							axios.post(url, _this.resourceEditForm).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.closeResourceEditDialog();
									_this.$message.success('保存成功!');
									_this.refreshResourceTree(1000);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 500);
					}else{
						return false;
					}
				});
			}
		},
		delResource: function(node){
			if(UPMS_RESOURCE_TYPE_SYSTEM == node.data.resourceType){
				this.$message.error('系统资源不能删除!');
			}else{
				var url = APP_UPMS_API.CONTEXT_PATH + '/resource/del/' + node.data.id;
				var _this = this;
				this.$confirm('你确定要删除该资源?', '提示', {
					confirmButtonText: '确定',
			        cancelButtonText: '取消',
			        type: 'warning',
			        callback: function(action, instance){
						if(action == 'confirm'){
							axios.get(url).then(function(response){
								var result = response.data;
								if(result.success){
									_this.$message.success('删除成功!');
									_this.refreshResourceTree(1000);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.$message.error('请求出错!');
							});
						}
					}
				});
			}
		},
		closeResourceEditDialog: function(){
			this.resourceEditDialogVisible = false;
			this.$refs.resourceEditForm.resetFields();
			this.resourceEditForm.appId = '';
			this.resourceEditForm.resourceId = '';
			this.resourceEditForm.parentResourceId = '';
			this.resourceEditForm.actionTypeEditable = true;
			this.resourceEditForm.indexPage = false;
		}
	},
	watch: {
	}
});
	