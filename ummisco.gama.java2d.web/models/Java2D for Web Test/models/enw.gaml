model TestJava2D
global {
    float aaa<-0.0;
    float aaaa<-0.0;
	file shp<-file("../includes/test.shp");
	image_file my_icon <- image_file("../includes/sheep.png");
// 	file shp<-file("../includes/landuse_myxuyen_2005_region.shp");
// 	geometry shape<-envelope(shp);
	init { 
// 		create parcel from:shp;
		create aa number:100;
        create parcel{
            shape<-rectangle(world.shape.width,world.shape.height) at_location {world.shape.width/2,world.shape.height/2};
        }
	}
	reflex ss{
	    float t<-machine_time;
	    ask aa{
	        do ss;
	    }
	    float tt<-machine_time-t;
	    if(tt>aaa){
	        aaa<-tt;
	    }
	}
} 
species aa skills:[moving]{
	rgb color  <- [100 + rnd (155),100 + rnd (155), 100 + rnd (155)] as rgb;
	float size<-1.0;
    action ss{
        do wander speed:2.0;
    }

	aspect icon {
		draw my_icon size:4 * size;
	}
	aspect default { 
		draw circle(size)  color:#red;// color;
	}
}
species parcel { 
}
grid cell width:10 height:10{
    
	float max_food <- 1.0;
	float food_prod <- rnd(0.01);
	float food <- rnd(1.0) max: max_food update: food + food_prod;
	rgb color <- rgb(int(255 * (1 - food)), 255, int(255 * (1 - food))) update: rgb(int(255 * (1 - food)), 255, int(255 * (1 - food)));
	reflex ss{
	    color <- rgb(rnd(255));//rgb(int(255 * (1 - food)), 255, int(255 * (1 - food))) update: rgb(int(255 * (1 - food)), 255, int(255 * (1 - food)));
	}
}
experiment TestExp type: gui autorun:true {
 
	output { 
		display o type: java2D synchronized:true { 
		    graphics s{
		        draw ""+aaa at:{10.0,10.0} color:#black;
		    }
		  //  image file:"../includes/sheep.png";
		  //  grid cell;
			 //species parcel{
			 //    draw shape color:rgb(rnd(255),rnd(255),rnd(255));
			 //}
		    species aa ;// aspect: icon;
		  //species parcel;
		}
	}
}
