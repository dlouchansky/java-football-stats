<#macro layout title>
<html>
   <head>
      <title>${title}</title>
      <link rel="stylesheet" href="kube.min.css"/>
   </head>
   <body>
   <div>
      <ul>
         <li><a href="/">Home</a></li>
         <li><a href="/upload">Upload</a></li>
         <li><a href="/top">Team top</a></li>
         <li><a href="/topPlayers">Player top</a></li>
         <li><a href="/topGoalkeepers">Goalkeeper top</a></li>
         <li><a href="/referees">Referee top</a></li>
      </ul>
   </div>

   <div>
      <h3>${title}</h3>
      <#nested>
   </div>

   </body>
</html>
</#macro>