<p>database name:moviedb</p>
<p>database user:testuser</p>
<p>password:testpass</p>

<p>We do searches for the movies through MoviesDAO.java which is movies database accessor.</p>
<p>When the user does a search for title, director, a star's first name, a star's last name, or a combination of those,</p>
<p>we take the user's input and pass it into MovieDAO.java's searchMovie() function.</p>
<p>Then with the parameters, we add a "like" and the wrap it in wild cards(%) within the function. This allows us to</p>
<p>pull out any words in moviedb that match the parameters by substring given in the searchMovie() function.</p>
