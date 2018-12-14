Vue.component('el-quill-editor', {
	componentName: 'ElQuillEditor',
	template: '<div class="el-quill-editor">'
	    		+ '<slot name="toolbar"></slot>'
	    		+ '<div ref="editor"></div>'
	    	+ '</div>',
	data: function(){
		return {
			quill: null,
			content: '',
			defaultModules: {
				toolbar: [
					['bold', 'italic', 'underline', 'strike'],
					['blockquote', 'code-block'],
					[{ 'header': 1 }, { 'header': 2 }],
					[{ 'list': 'ordered'}, { 'list': 'bullet' }],
					[{ 'script': 'sub'}, { 'script': 'super' }],
					[{ 'indent': '-1'}, { 'indent': '+1' }],
					[{ 'direction': 'rtl' }],
					[{ 'size': ['small', false, 'large', 'huge'] }],
					[{ 'header': [1, 2, 3, 4, 5, 6, false] }],
					[{ 'color': [] }, { 'background': [] }],
					[{ 'font': [] }],
					[{ 'align': [] }],
					['clean'],
					['link', 'image', 'video']
				]
	        }
		};
	},
	props: {
		value: {
			type: String,
			default: ''
		},
	    disabled: Boolean,
	    options: {
	        type: Object,
	        required: false,
	        default: function() {
	        	return {};
	        }
	    }
	},
	mounted: function(){
		this.init();
	},
	methods: {
		init: function(){
			if(window.Quill){
				this.options.theme = this.options.theme || 'snow';
		        this.options.boundary = this.options.boundary || document.body;
		        this.options.modules = this.options.modules || this.defaultModules;
		        this.options.modules.toolbar = this.options.modules.toolbar !== undefined ? this.options.modules.toolbar : this.defaultModules.toolbar;
		        this.quill = new Quill(this.$refs.editor, this.options);
		        if(this.value){
		        	this.content = this.value;
		        	this.quill.pasteHTML(this.value);
		        }
		        var _this = this;
		        // editor content changed
		        this.quill.on('text-change', function(delta, oldDelta, source){
		        	var html = _this.$refs.editor.children[0].innerHTML;
		            var text = _this.quill.getText();
		            if (html === '<p><br></p>') {
		            	html = '';
		            }
		            _this.content = html;
		            _this.value = html;
		            _this.$emit('input', _this.value);
		            _this.$emit('change', {
		            	editor: _this.quill,
		                html: html,
		                text: text
		            });
				});
		        // editor selection changed
		        this.quill.on('selection-change', function(range, oldRange, source) {
		            if (!range) {
		            	_this.$emit('blur', _this.quill, _this.value);
		            } else {
		            	_this.$emit('focus', _this.quill, _this.value);
		            }
		        });
		        // disabled
		        if (this.disabled) {
		            this.quill.enable(false);
		        }
		        // emit ready
		        this.$emit('ready', this.quill, this.value);
			}
		}
	},
	watch: {
		value: function(newVal, oldVal){
			if (this.quill) {
				if(this.content !== newVal){
					this.quill.pasteHTML(newVal);
				}
	        }
		},
		disabled: function(newVal, oldVal){
			if (this.quill) {
				this.quill.enable(!newVal);
	        }
		}
	}
});