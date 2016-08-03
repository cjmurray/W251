## W251 HW 12
Chris Murray

### Data
The data is the Colorado voter registration database, available at http://coloradovoters.info/ .


### ElasticSearch GUI
I attempted to set up the ElasticSearch GUI but had a lot of difficulty.  Nevertheless, it is accessible at

  http://50.97.196.178:9200/_plugin/gui/index.html 


### Interesting queries

See if there are any voters with the name "Barack":

  http://50.97.196.178:9200/voters/_search?q=voter_name:BARACK&pretty

See if there are any voters with house number 12345:

  http://50.97.196.178:9200/voters/_search?q=house_num:12345&pretty

Look up phone number 303-455-1211: (Oh, hello Governor!)

  http://50.97.196.178:9200/voters/_search?q=phone_num:3034551211&pretty

See if any voters are over 100 years old:

  http://50.97.196.178:9200/voters/_search?q=birth_year:[1900+TO+1915]&pretty

Who lives next door to me?

  http://50.97.196.178:9200/voters/_search?q=residential_address:%225824E%2010TH%20AVE%22&pretty
