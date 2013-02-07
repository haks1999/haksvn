
/*
 * prototype
 */
String.prototype.trim = function() {
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
};

/*
 * jquery extend
 */
jQuery.extend({postJSON: function(url, data, callback){$.post(url, data, callback, "json");}});