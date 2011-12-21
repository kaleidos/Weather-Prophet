$(document).ready(function() {
    //$("#slider").slider();
    
		$("#sliderRain").slider({
            range: "min",
			value:$("#amountRain").text(),
			min: 0,
			max: 100,
			step: 1,
			slide: function(event, ui) {
				$("#amountRain").text(ui.value);
                $("#probability").val(ui.value);
			}
		});
        
		
})
