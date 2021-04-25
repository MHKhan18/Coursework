
(function($) {

    let showList = $('#showList');
    let showDiv = $('#show');
    let searchForm = $('#searchForm');
    let input = $('#search_term');
    let homeLink = $('#homeLink');
   
    homeLink.hide();

  
    $.get('http://api.tvmaze.com/shows' , function(shows){
        shows.forEach((show) => {
            let link = `<li><a href="${show._links.self.href}">${show.name}</a></li>`;
            showList.append(link);
        });    

        showDiv.hide();

        // need to bind events to each link
        showList.children().each(function(index, element) {
            element.addEventListener('click' , linkEventListener);
        });

        showList.show();
    });

    

    searchForm.submit(function(event) {
        event.preventDefault();

        let query = input.val();
        query = query.trim(); // can't be length zero bc 'required'
        
        showList.empty();

        $.get(`http://api.tvmaze.com/search/shows?q=${query}` , function(shows){
            shows.forEach((data) => {
                let show = data.show;
                let link = `<li><a href="${show._links.self.href}">${show.name}</a></li>`;
                showList.append(link);
            });

            showDiv.hide();

            // need to bind events to each link
            showList.children().each(function(index, element) {
                element.addEventListener('click' , linkEventListener);
            });

            showList.show();
        });

        homeLink.show();
        
    });

    function linkEventListener(event){
        event.preventDefault();

        showList.hide();
        showDiv.empty();

        let endpoint = $(event.target).attr("href");
        
        let name , image , lang , genres , rating , network , summary;

        $.get(endpoint , function(theShow){
            name = theShow?.name ?? "N/A";
            image = theShow?.image?.medium ?? "/public/img/no_image.jpeg";
            lang = theShow?.language ?? "N/A";
            genres = theShow?.genres ?? ["N/A"];
            rating = theShow?.rating?.average ?? "N/A";
            network = theShow?.network?.name ?? "N/A";
            summary = theShow?.summary ?? "N/A";
            summary = summary.replace(/(<([^>]+)>)/gi, ""); // takeout html

            let genresList = '<ul>';
            for(let genre of genres){
                genresList += `<li>${genre}</li>`;
            }
            genresList += '</ul>';

            let content = `
                <h1>${name}</h1>
                <img src=${image} alt="Show Placard">
                <dl>
                    <dt>Language:</dt>
                    <dd>${lang}</dd>

                    <dt>Genres:</dt>
                    <dd>${genresList}</dd>

                    <dt>Rating:</dt>
                    <dd>${rating}</dd>

                    <dt>Network:</dt>
                    <dd>${network}</dd>

                    <dt>Summary:</dt>
                    <dd>${summary}</dd>
                </dl>
            `;

            showDiv.append(content);
            showDiv.show();
        });
        homeLink.show();
        
    }
})(window.jQuery);
