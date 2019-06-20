package dr.inferencexml.operators.shrinkage;

import dr.evomodel.branchratemodel.AutoCorrelatedBranchRatesDistribution;
import dr.inference.distribution.DistributionLikelihood;
import dr.inference.distribution.shrinkage.BayesianBridgeStatisticsProvider;
import dr.inference.operators.shrinkage.BayesianBridgeShrinkageOperator;
import dr.math.distributions.GammaDistribution;
import dr.xml.*;

import static dr.inference.operators.MCMCOperator.WEIGHT;

public class BayesianBridgeShrinkageOperatorParser extends AbstractXMLObjectParser {

    public final static String BAYESIAN_BRIDGE_PARSER = "bayesianBridgeGibbsOperator";

    @Override
    public Object parseXMLObject(XMLObject xo) throws XMLParseException {

        double weight = xo.getDoubleAttribute(WEIGHT);

        BayesianBridgeStatisticsProvider bayesianBridge =
                (BayesianBridgeStatisticsProvider) xo.getChild(BayesianBridgeStatisticsProvider.class);

        DistributionLikelihood prior = (DistributionLikelihood) xo.getChild(DistributionLikelihood.class);
        if (!(prior.getDistribution() instanceof GammaDistribution)) {
            throw new XMLParseException("Gibbs sampler only implemented for a gamma distributed global scale");
        }
        GammaDistribution globalScalePrior = (GammaDistribution) prior.getDistribution();

        return new BayesianBridgeShrinkageOperator(bayesianBridge, globalScalePrior, weight);
    }

    @Override
    public XMLSyntaxRule[] getSyntaxRules() {
        return rules;
    }

    private XMLSyntaxRule[] rules = new XMLSyntaxRule[]{
            AttributeRule.newDoubleRule(WEIGHT),
            new OrRule(
                    new ElementRule(BayesianBridgeStatisticsProvider.class),
                    new ElementRule(AutoCorrelatedBranchRatesDistribution.class)
            ),
            new ElementRule(DistributionLikelihood.class),
    };

    @Override
    public String getParserDescription() {
        return null;
    }

    @Override
    public Class getReturnType() {
        return BayesianBridgeShrinkageOperator.class;
    }

    @Override
    public String getParserName() {
        return BAYESIAN_BRIDGE_PARSER;
    }
}
