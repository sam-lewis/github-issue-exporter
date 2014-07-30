import au.com.bytecode.opencsv.CSVWriter
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import org.glassfish.jersey.jackson.JacksonFeature

import javax.ws.rs.client.ClientBuilder

if (args.length != 3) {
    println 'usage: github-export [username] [password] [repository]'
    return
}

def user = args[0]
def password = args[1]
def repository = args[2]

def client = ClientBuilder.newBuilder().build()
        .register(HttpAuthenticationFeature.basic(user, password))
        .register(new JacksonFeature())

def target = client.target("https://api.github.com/repos/$repository/issues")
        .queryParam('filter', 'all')
        .queryParam('state', 'all')
        .queryParam('per_page', '100')

def allIssues = []
def done = false
def page = 1

while (!done) {
    println "Loading page $page..."

    def issues = target.queryParam('page', page.toString()).request().get(List)

    if (issues.size() > 0) {
        allIssues += issues
    }

    if(issues.size() < 100) {
        done = true
    }
    page++
}

println "Loaded $allIssues.size issues"

def outFile = new File('issues.csv')
println "Writing CSV: $outFile.absolutePath"
def csvWriter = new CSVWriter(new FileWriter(outFile))

def headers = ['number', 'url', 'title', 'state', 'assignee.login'] as String[]
csvWriter.writeNext(headers)
allIssues.each { issue ->
    def items = headers.collect { header ->
        def result = issue
        header.tokenize('.').each {
            result = result?."${it}"
        }
        result
    }
    csvWriter.writeNext(items as String[])
}
csvWriter.close()



