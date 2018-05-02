/**
 * @Generated
 */
(function() {
	rap.registerTypeHandler("ummisco.gama.participative.EtherpadBasicText", {
		
		factory : function(properties) {
			console.log(" --> rap.registerTypeHandler(ummisco.gama.participative.EtherpadBasicText   -> from Gaml");
			return new ummisco.gama.participative.EtherpadBasicText(properties);
		},
		destructor : "destroy",
		properties : [ "url", "text", "editable", "status", "annotations", "scope", "proposals", "font", "dirty", "markers", "background"],
		events : ["Modify", "TextChanged", "Save", "FocusIn", "FocusOut", "Selection", "CaretEvent", "ContentAssist"],
		methods : ["setSelection", "addMarker", "removeMarker", "clearMarkers", "insertText", "removeText", "setProposals", "moveCursorFileStart","moveCursorFileEnd"]
	});
	
//------ begin of the Added Code -------------------------------------------------------------------------------
	rap.registerTypeHandler("o7planning.EtherpadComposite", {
		
		   factory : function(properties) {
			   console.log(" --> rap.registerTypeHandler(o7planning.EtherpadComposite   -> from Ether");
	          return new o7planning.EtherpadComposite(properties);
		   },
		   destructor : "destroyEtherpad",
		   properties : [ "abc" ],
		   events : [],
		   methods : [ 'appendWarn', 'appendErr', 'appendInfo', 'setText', 'createAndMergeEditors', 'clearAll' ]
		});

	   if (!window.o7planning) {
		   console.log(" --> ");
	       window.o7planning = {};
	   }
	   
	   // Constructor
	   o7planning.EtherpadComposite = function(properties) {
		   console.log(" --> o7planning.EtherpadComposite = function(properties)    -> from Ether");
	       bindAll(this, [ "layout", "onReady", "onSend", "onRender", "onChange" ]);// @custom
	       this.parent = rap.getObject(properties.parent);
	       this.element = document.createElement("div");
	       this.parent.append(this.element);
	       this.parent.addListener("Resize", this.layout);
	
	       this.etherpadjs = new EtherpadJS(this.element);
	       // Render interface
	       rap.on("render", this.onRender);
	    
	      
	   };

	   
	   o7planning.EtherpadComposite.prototype = {
			  
		       ready : false,
		       onChange : function() {      },
		       onReady : function() {       },
		       onRender : function() {
		    	   
		           if (this.element.parentNode) {
		        	   console.log(" --> o7planning.EtherpadComposite.prototype =   -> from Ether");
		               rap.off("render", this.onRender);
		               rap.on("render", this.onRender);
		               rap.on("send", this.onSend);
		              
		           }
		       },
		       onSend : function() {    },
		 
		       destroyEtherpad : function() {
		    	   console.log(" --> destroy From Etherpad: function()   -> from Ether");
		           rap.off("send", this.onSend);
		           try {
		        	  this.element.parentNode.removeChild(this.element);
		           } catch (e) {
		               try {
		                   console
		                           .log('error call this.element.parentNode.removeChild(this.element) :'
		                                   + e);
		               } catch (e) {
		               }
		           }
		       },
		       layout : function() { 
		           if (this.ready) { 
		               var area = this.parent.getClientArea();
		               this.element.style.left = area[0] + "px";
		               this.element.style.top = area[1] + "px";
		               this.editor.resize(area[2], area[3]);
		               console.log(" --> layout : function()    -> from Ether");
		           }
		       },
		       setAbc : function(abc) {  console.log(" --> setAbc : function(abc)   -> from Ether" );    },
		       appendErr : function(json) {  
		    	   console.log(" --> appendErr   -> from Ether");
		           var text= json["text"];
		           this.etherpadjs.appendErr(text);
		       },
		       appendWarn : function(json) {  
		    	   console.log(" --> appendWarn   -> from Ether");
		           var text= json["text"];
		           this.etherpadjs.appendWarn(text);
		       },
		       appendInfo : function(json) {  
		    	   console.log(" --> appendInfo   -> from Ether");
		           var text= json["text"];
		           var userId= json["userId"];
		           this.etherpadjs.appendInfo(text, userId);
		       },
		       setText : function(json) {  
		    	   console.log(" --> setText   -> from Ether");
		           var text= json["text"];
		           var userId= json["userId"];
		           var padId= json["padId"];
		           console.log('call setText');
		           this.etherpadjs.setText(text, userId, padId);
		       },
		       createAndMergeEditors : function(json) {  
		    	   console.log(" --> createAndMergeEditors   -> from Ether");
		           var text= json["text"];
		           var userId= json["userId"];
		           var padId= json["padId"];
		           this.etherpadjs.createAndMergeEditors(text, userId, padId);
		       },
		       clearAll : function()  {  
		    	   console.log(" --> clearAll  -> from Ether");
		           this.etherpadjs.clearAll();
		       }
		   };
	   
	   var bind = function(context, method) {
		   //console.log(" --> bind");
	       return function() {
	           return method.apply(context, arguments);
	       };
	   };
	   var bindAll = function(context, methodNames) {
		  // console.log(" --> bindAll");
	       for (var i = 0; i < methodNames.length; i++) {
	           var method = context[methodNames[i]];
	           context[methodNames[i]] = bind(context, method);
	       }
	   };
	   var async = function(context, func) {
		//   console.log(" --> async");
	       window.setTimeout(function() {
	           func.apply(context);
	       }, 0);
	   };
//-------------------------------------------------------------------------------------
// ----- end of the Added Code --------------------------------------------------------
	   
	
	rwt.qx.Class.define("ummisco.gama.participative.EtherpadBasicText", {
		extend :org.dslforge.styledtext.BasicText,
		construct : function(properties) {
			console.log(" --> rwt.qx.Class.define(    -> from Gaml");
			this.base(arguments, properties);
		},
		members : {
			createEditor : function() {
				console.log(" --> createEditor : function()   -> from Gaml");
				var basePath = 'rwt-resources/src-js/org/dslforge/styledtext/ace';
				ace.require("ace/config").set("basePath", basePath);
				var workerPath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("workerPath", workerPath);
				var themePath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("themePath", themePath);
				var modePath = 'rwt-resources/src-js/ummisco/gama/participative/ace';
				ace.require("ace/config").set("modePath", modePath);
				var editor = this.editor = ace.edit(this.element);
				if (editor != null) {
					console.log(" --> if (editor != null)   -> from Gaml");
					console.log("Editor created! EtherGaml.js");
					var editable = this.editable;
					var guid = this.url;
		        	var self = this;
					ace.config.loadModule("ace/ext/language_tools", function (module) {
						editor.getSession().setMode("ace/mode/gaml");
						editor.getSession().id = self.url;	
			        	editor.setOptions({
				            enableBasicAutocompletion: true,
				            enableTextCompleter: false,
				            enableKeyWordCompleter: false,
				            enableSnippets: false,
						    useWorker: true,
			            });	
			        	self.langTools = ace.require("ace/ext/language_tools");
						self.serverCompleter = {
							getMode: function() {
								return editor.getSession().getMode();
							},
							getCompletions: function(editor, session, pos, prefix, callback) {
								self.onCompletionRequest(pos, prefix, callback);
							},
							getDocTooltip: function(item) {
						    	item.docHTML = ["<div class=\"ace_line\" style=\"height:12px\"><span class=\"", self.typeToIcon(item.meta),"\">&nbsp;</span><span class=\"ace_\">","<b>", item.caption, "</b>","</span><span class=\"ace_rightAlignedText\"></span></div>", "<hr></hr>", item.meta].join("");
							}
						};
						console.log(" --> editor.setOption   -> from Gaml");
			        	editor.setOption("enableServerCompleter", self.serverCompleter);
			        	self.completers = editor.completers;
						editor.setTheme("ace/theme/gaml");
						editor.setShowPrintMargin(false);
						editor.setBehavioursEnabled(true);
						editor.setWrapBehavioursEnabled(true);
						editor.setReadOnly(!editable);
						editor.setFontSize(12);
						editor.$blockScrolling = Infinity;
			        });
					//Session settings
					console.log(" -->  //Session settings   -> from Gaml");
					var session = editor.getSession();
					editor.getSession().setUseWrapMode(true);
				    editor.getSession().setTabSize(4);
				    editor.getSession().setUseSoftTabs(true);
					editor.getSession().getUndoManager().reset();
					//Text hover
					
					var TokenTooltip = ace.require("ace/ext/tooltip").TokenTooltip;
					editor.tokenTooltip = new TokenTooltip(editor);
					//Annotations
					if (this.annotations==null)	this.annotations=[];
					//Index
				 	index = this.scope;
				 	proposals = this.proposals;
				 	if (this.useSharedWorker) {
						if (typeof SharedWorker == 'undefined') {
								console.log("Your browser does not support Javascript shared workers, as a consequence some features will be disabled."
								+ "For a full-featured user experience, the following browsers are supported: Chrome, Firefox, Safari.");
							} else {
							var filePath = 'rwt-resources/src-js/org/dslforge/styledtext/global-index.js';
							var httpURL = this.computeWorkerPath(filePath);
							var worker = this.worker = new SharedWorker(httpURL);
							if (this.ready) {
								editor.on("change", function(event) {
									worker.port.postMessage({
										message: editor.getValue(),
								        guid: guid,
								        index: index
								    });
							    });
							}
							worker.port.onmessage = function(e) {
							 	index = e.data.index;
						    };
						}
				 	}
					editor.on("focus", function() {
						console.log(" --> editor.on focus   -> from Gaml");
				 		self.onFocus();
				 	});
				 	editor.on("blur", function() {
				 		console.log(" --> editor.on blur   -> from Gaml");
				 		self.onBlur();
				 	});
				 	editor.on("input", function() {
				 		console.log(" --> editor.on input   -> from Gaml");
						if (!editor.getSession().getUndoManager().isClean())
							
							
						
						
						
			               	self.onModify();
				 	});
				 	editor.getSession().getSelection().on('changeCursor', function() {
				 		console.log(" --> editor.getSession().getSelection().on   -> from Gaml");
				 	    self.onChangeCursor();
				 	});
					editor.commands.addCommand({
						name: 'saveFile',
						bindKey: {win: 'Ctrl-S', mac: 'Command-S', sender: 'editor|cli'},
						exec: function(env, args, request) {
							console.log(" --> editor.commands.addCommand   -> from Gaml");
							self.onSave();
						}
					});
				}
				//Done
		        this.onReady();
			},
			setScope : function(scope) {
				console.log(" --> setScope   -> from Gaml");
				this.base(arguments, scope);
			},
			onCompletionRequest : function(pos, prefix, callback) {
				console.log(" --> onCompletionRequest   -> from Gaml");
				this.base(arguments, pos, prefix, callback);
			},
			setProposals : function(proposals) {
				console.log(" --> setProposals   -> from Gaml");
				this.proposals = proposals;
			},
			onFocus: function() { 
				console.log(" --> onFocus   -> from Gaml");
				this.base(arguments);
			},
			onBlur: function() {
				console.log(" --> onBlur -> from Gaml");
				this.base(arguments);
			},
			destroy : function() {
				console.log(" --> destroy  -> from Gaml");
				this.base(arguments);
				this.langTools.resetOptions(this.editor);
			}
		}
	});
}());