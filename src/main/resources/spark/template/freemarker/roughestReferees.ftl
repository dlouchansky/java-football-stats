<#import "layout.ftl" as u>

    <@u.layout title="Referee top by given cards">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Cards average for game</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td>${result.nr}</td>
                <td>${result.name}</td>
                <td>${result.cardGameAverage?string["0.#"]}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
